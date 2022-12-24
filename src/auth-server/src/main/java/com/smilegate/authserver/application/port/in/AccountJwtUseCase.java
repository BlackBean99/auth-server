package com.smilegate.authserver.application.port.in;

import com.smilegate.authserver.domain.dto.LoginResponseDto;

public interface AccountJwtUseCase {
    public LoginResponseDto login(String email, String password);
}
