package com.smilegate.authserver.adapter.out.persistence;

import com.smilegate.authserver.application.port.out.LoadUserPort;
import com.smilegate.authserver.domain.user.User;
import com.smilegate.authserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort,RecordUserPort {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> loadByEmal(String email) {
        return userRepository.findByEmail(email);
    }

    public User loadUserByUserEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }
}
