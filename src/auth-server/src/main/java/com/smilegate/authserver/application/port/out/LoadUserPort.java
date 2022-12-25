package com.smilegate.authserver.application.port.out;

import com.smilegate.authserver.domain.user.User;

import java.util.Optional;

public interface LoadUserPort {
    boolean existsByEmail(String email);
    Optional<User> loadByEmal(String email);

}
