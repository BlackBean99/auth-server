package com.smilegate.authserver.application.service;

import com.smilegate.authserver.application.port.in.AccountSignUpUseCase;
import com.smilegate.authserver.application.port.out.LoadUserPort;
import com.smilegate.authserver.application.port.out.RecordUserPort;
import com.smilegate.authserver.domain.auth.ConfirmationToken;
import com.smilegate.authserver.domain.auth.ConfirmationTokenRepository;
import com.smilegate.authserver.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AccountSignUpService implements AccountSignUpUseCase {
    private final PasswordEncoder passwordEncoder;
    private final LoadUserPort loadUserPort;
    private final RecordUserPort recordUserPort;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    //   회원가입
    @Override
    @Transactional
    public void signUp(String userName, String userEmail, String password) {
        // 중복검증
        String encodePassword = passwordEncoder.encode(password);
        User newUser = new User(userName, encodePassword, userEmail);
        recordUserPort.save(newUser);
    }

    // 중복된 이메일 확인
    @Override
    public boolean isDuplicateEmail(String email) {
        return loadUserPort.existsByEmail(email);
    }

    @Override
    public void confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findById(UUID.fromString(token)).orElseThrow(() -> new IllegalArgumentException("인증받지 못한 유저입니다."));
        User user = loadUserPort.loadByEmal(confirmationToken.getEmail());
        user.isEnabled();
    }
}

