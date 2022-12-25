package com.smilegate.authserver.application.port.in;

import com.smilegate.authserver.domain.auth.ConfirmationToken;

import java.util.UUID;

public interface ConfirmationTokenUseCase {
    public UUID createEmailConfirmationToken(String receiverEmail);
//    public String createEmailConfirmationToken(String receiverEmail);
    public ConfirmationToken findByIdAndExpirationDateAfterAndExpired(UUID confirmationTokenId);
}
