package com.smilegate.authserver.domain.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    @Email
    private String userEmail;

    private String password;

    private String redirectUrl;
}
