package com.smilegate.authserver.application.service;

import com.smilegate.authserver.application.port.in.AccountSignUpUseCase;
import com.smilegate.authserver.domain.user.User;
import com.smilegate.authserver.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AccountSignUpService implements AccountSignUpUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    //   회원가입
    @Override
    @Transactional
    public void signUp(String userName, String userEmail, String password) {
        // 중복검증
        String encodePassword = passwordEncoder.encode(password);
        User newUser = new User(userName,encodePassword,userEmail);
        userRepository.save(newUser);
    }

}

