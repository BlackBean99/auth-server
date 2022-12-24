package com.smilegate.authserver.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface JwtProviderUseCase {
    String createAccessToken(String email, String role);

    String createRefreshToken(String email, String role);

    Date getExpiredTime(String refreshToken);

    Authentication validateToken(HttpServletRequest request, String refreshToken);

    void logout(String refreshToken);

}
