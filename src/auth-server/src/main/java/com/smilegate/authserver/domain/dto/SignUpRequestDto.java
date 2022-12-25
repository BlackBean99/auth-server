package com.smilegate.authserver.domain.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    @Email
    private String email;

    private Long year;

    private String name;

    private String password;
}
