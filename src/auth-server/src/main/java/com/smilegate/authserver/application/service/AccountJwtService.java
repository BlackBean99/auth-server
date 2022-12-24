package com.smilegate.authserver.application.service;

import com.smilegate.authserver.adapter.out.persistence.UserPersistenceAdapter;
import com.smilegate.authserver.application.port.in.AccountJwtUseCase;
import com.smilegate.authserver.application.port.in.JwtProviderUseCase;
import com.smilegate.authserver.domain.dto.LoginResponseDto;
import com.smilegate.authserver.domain.user.User;
import com.smilegate.authserver.domain.user.UserRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountJwtService implements AccountJwtUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserPersistenceAdapter accountPersistenceAdapter;
    private final JwtProviderUseCase jwtProviderUseCase;

    @Override
    @Transactional
    public LoginResponseDto login(String email, String password) {
        User account = accountPersistenceAdapter
                .loadUserByUserEmail(email);
        checkPassword(password, account.getPassword());
        return createToken(account);
    }
    @Transactional
    public LoginResponseDto createToken(User user){
        String accessToken = jwtProviderUseCase.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtProviderUseCase.createRefreshToken(user.getEmail(), user.getRole());
        return new LoginResponseDto(accessToken, refreshToken);
    }

    private void checkPassword(String password, String encodePassword) {
        boolean isMatch = passwordEncoder.matches(password, encodePassword);
        if(!isMatch){
            throw new BadRequestException("아이디 혹은 비밀번호를 확인하세요");
        }
    }
}
