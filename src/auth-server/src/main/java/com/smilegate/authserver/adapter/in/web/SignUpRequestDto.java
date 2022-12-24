package com.smilegate.authserver.adapter.in.web;

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
