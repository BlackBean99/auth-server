package com.smilegate.authserver.adapter.in.web;

import com.smilegate.authserver.application.port.in.AccountJwtUseCase;
import com.smilegate.authserver.application.port.in.AccountSignUpUseCase;
import com.smilegate.authserver.application.port.in.JwtProviderUseCase;
import com.smilegate.authserver.domain.dto.LoginRequestDto;
import com.smilegate.authserver.domain.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Account 관련 서비스", description = "회원가입, 로그인 등등")
public class AccountController {
    private final AccountSignUpUseCase accountSignUpUseCase;
    private final JwtProviderUseCase jwtProviderUseCase;
    private final AccountJwtUseCase accountJwtUseCase;
    //  회원가입 기능
    @Operation(summary = "회원가입", description = "회원 가입")
    @ApiResponse(responseCode = "HttpStatus.CREATED", description = "CREATED")
    @PostMapping("/api/accounts")
    public ResponseEntity<String> signUp(@Valid SignUpRequestDto signUpUser) {
        accountSignUpUseCase.isDuplicateEmail(signUpUser.getEmail());
        accountSignUpUseCase.signUp(signUpUser.getName(), signUpUser.getEmail(), signUpUser.getPassword());
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }

    @Operation(summary = "이메일 중복 확인", description = "회원 가입시 이메일 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "HttpStatus.ACCEPTED", description = "CREATED")
    })
    @GetMapping("/api/accounts/check/email")
    public ResponseEntity<String> checkDuplicated(String userEmail) {
        if(accountSignUpUseCase.isDuplicateEmail(userEmail)){
            return new ResponseEntity<>("중복된 이메일입니다", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("사용할 수 있는 이메일입니다.", HttpStatus.ACCEPTED);
    }

    @PostAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "토큰 사용가능 여부 확인", description = "Access Token 사용 가능 여부 확인")
    @ApiResponses({
            @ApiResponse(description = "access, refreshToken"),
            @ApiResponse(responseCode = "HttpStatus.OK", description = "OK")
    })
    @GetMapping("/api/accounts/re-check")
    public ResponseEntity<String> checkValidToken(HttpServletRequest request, String refreshToken) {
        Authentication authentication = jwtProviderUseCase.validateToken(request, refreshToken);
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
//        토큰 형식이 잘못되면 BadRequest 반환 예외처리 추가예정
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    // 로그인 인증
    @Operation(summary = "로그인 페이지 처리", description = "로그인완료 후 원래 페이지로 이동")
    @ApiResponses({
            @ApiResponse(description = "access, refreshToken"),
            @ApiResponse(responseCode = "HttpStatus.OK", description = "로그인 내부 인증 처리")
    })
    @PostMapping("/api/accounts/login/process")
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginDto) throws URISyntaxException {
        LoginResponseDto responseDto = accountJwtUseCase.login(loginDto.getUserEmail(), loginDto.getPassword());
        URI redirectUri = new URI(loginDto.getRedirectUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        log.info("redirectUrl" + redirectUri);
        Date expiredTime = jwtProviderUseCase.getExpiredTime(responseDto.getRefreshToken());
        Map<Date, LoginResponseDto> loginResponseDto = new HashMap<>();
        loginResponseDto.put(expiredTime, responseDto);
        return new ResponseEntity<>(responseDto, httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "토큰 재발행", description = "Refresh 토큰이 유효하면, Access Token,Refresh Token 재발행")
    @ApiResponses({
            @ApiResponse(description = "access, refreshToken"),
            @ApiResponse(responseCode = "HttpStatus.OK", description = "OK")
    })
    @GetMapping("/api/accounts/re-issue")
    public ResponseEntity<LoginResponseDto> reIssue(@Email String userEmail, String refreshToken, HttpServletRequest request) {
        if(!jwtProviderUseCase.validateToken(request,refreshToken).isAuthenticated()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LoginResponseDto responseDto = accountJwtUseCase.reIssueAccessToken(userEmail, refreshToken);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //    로그아웃 기능 구현
    @Operation(summary = "logout", description = "로그아웃_에이전트, 로그아웃시 redirect 페이지로 이동")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "OK")
    @GetMapping("/api/accounts/logout")
    public ResponseEntity<String> logout(String redirectUrl, HttpServletRequest request) throws URISyntaxException {
//        7번부터 빼야 bearer(+스페이스바) 빼고 토큰만 추출 가능
        String refreshToken = request.getHeader("Authorization").substring(7);
        accountJwtUseCase.logout(refreshToken);
        URI redirectUri = new URI(redirectUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>("", httpHeaders, HttpStatus.OK);
    }

}
