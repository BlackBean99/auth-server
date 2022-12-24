package com.smilegate.authserver.adapter.in.web;

import com.smilegate.authserver.application.port.in.AccountSignUpUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Account 관련 서비스", description = "회원가입, 로그인 등등")
public class AccountController {
    private final AccountSignUpUseCase accountSignUpUseCase;
    //  회원가입 기능 구현
    @Operation(summary = "회원가입", description = "회원 가입")
    @ApiResponse(responseCode = "HttpStatus.CREATED", description = "CREATED")
    @PostMapping("/api/account")
    public ResponseEntity<String> signUp(@Valid SignUpRequestDto signUpUser) {
        accountSignUpUseCase.signUp(signUpUser.getName(), signUpUser.getEmail(), signUpUser.getPassword());
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }
}
