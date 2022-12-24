package com.smilegate.authserver.global.common.config.jwt;

import com.smilegate.authserver.application.port.in.JwtProviderUseCase;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider implements JwtProviderUseCase {

    private UserDetailsService customUserDetailsService;
    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.jwt.blacklist.access-token}")
    private String blackListATPrefix;

    private final RedisService redisService;

    @Override
    public String createAccessToken(String userId, String role) {
        long tokenInvalidTime = 1000L * 60 * 3;//3m
        return this.createToken(userId, role, tokenInvalidTime);
    }

    private String createToken(String userEmail, String role, long tokenInvalidTime) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 발행유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenInvalidTime)) // 토큰 유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) // 해싱 알고리즘 및 키 설정
                .compact();
    }

    @Override
    public String createRefreshToken(String email, String role) {
        Long tokenInvalidTime = 1000L * 60 * 60 * 24; // 1d
        String refreshToken = this.createToken(email, role, tokenInvalidTime);
        redisService.setValues(email, refreshToken, Duration.ofMillis(tokenInvalidTime));
        return refreshToken;
    }
    @Override
    public Date getExpiredTime(String token) {
        try {
            return (Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()).getExpiration();
        }catch (Exception e) {
            return null;
        }
    }
    @Override
    public Authentication validateToken(HttpServletRequest request, String token) {
        String exception = "exception";
        try {
            String expiredAT = redisService.getValues(blackListATPrefix + token);
            if (expiredAT != null) {
                throw new ExpiredJwtException(null, null, null);
            }
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return getAuthentication(token);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            request.setAttribute(exception, "토큰의 형식을 확인하세요");
        } catch (ExpiredJwtException e) {
            request.setAttribute(exception, "토큰이 만료되었습니다.");
        } catch (IllegalArgumentException e) {
            request.setAttribute(exception, "JWT compact of handler are invalid");
        }
        return null;
    }
    private Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    private String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
