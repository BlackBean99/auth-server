package com.smilegate.authserver.global.common.config.jwt;

import com.smilegate.authserver.application.port.in.JwtProviderUseCase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.Date;


@RequiredArgsConstructor
public class JwtProvider implements JwtProviderUseCase {
    @Value("${spring.jwt.secret-key}")
    private String secretKey;

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
}
