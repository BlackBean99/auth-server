package com.smilegate.authserver.adapter.in.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;
}
