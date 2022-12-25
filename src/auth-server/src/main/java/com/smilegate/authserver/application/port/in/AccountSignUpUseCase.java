package com.smilegate.authserver.application.port.in;

public interface AccountSignUpUseCase {
    public void signUp(String name, String email, String password);
    public boolean isDuplicateEmail(String email) ;
    public void confirmAccount(String token);
    }
