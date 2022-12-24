package com.smilegate.authserver.application.port.in;

public interface JwtProviderUseCase {
    String createAccessToken(String email, String role);

    String createRefreshToken(String email, String role);
}
