package com.smilegate.authserver.adapter.in.web;

import com.smilegate.authserver.application.port.in.AccountJwtUseCase;
import com.smilegate.authserver.application.port.in.AccountSignUpUseCase;
import com.smilegate.authserver.domain.dto.LoginRequestDto;
import com.smilegate.authserver.domain.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final AccountJwtUseCase accountJwtUseCase;
    //  회원가입 기능
    @Operation(summary = "회원가입", description = "회원 가입")
    @ApiResponse(responseCode = "HttpStatus.CREATED", description = "CREATED")
    @PostMapping("/api/account")
    public ResponseEntity<String> signUp(@Valid SignUpRequestDto signUpUser) {
        accountSignUpUseCase.isDuplicateEmail(signUpUser.getEmail());
        accountSignUpUseCase.signUp(signUpUser.getName(), signUpUser.getEmail(), signUpUser.getPassword());
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }

    @Operation(summary = "이메일 중복 확인", description = "회원 가입시 이메일 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "HttpStatus.ACCEPTED", description = "CREATED")
    })
    @GetMapping("/api/account/check/email")
    public ResponseEntity<String> checkDuplicated(String userEmail) {
        if(accountSignUpUseCase.isDuplicateEmail(userEmail)){
            return new ResponseEntity<>("중복된 이메일입니다", HttpStatus.FORBIDDEN)
        }
        return new ResponseEntity<>("사용할 수 있는 이메일입니다.", HttpStatus.ACCEPTED);
    }


    // 로그인 인증
    @Operation(summary = "로그인 페이지 처리", description = "로그인완료 후 원래 페이지로 이동")
    @ApiResponses({
            @ApiResponse(description = "access, refreshToken"),
            @ApiResponse(responseCode = "HttpStatus.OK", description = "로그인 내부 인증 처리")
    })
    @PostMapping("/api/account/login/process")
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


}
