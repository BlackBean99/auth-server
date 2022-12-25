package com.smilegate.authserver.application.port.out;

import com.smilegate.authserver.domain.user.User;

import java.util.List;

public interface LoadUserPort {
    boolean existsByEmail(String email);
    User loadByEmal(String email);
    List<User> loadAll(Integer page);
    User findById(Long id);
}
