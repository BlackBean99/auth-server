package com.smilegate.authserver.application.service;

import com.smilegate.authserver.adapter.out.persistence.UserPersistenceAdapter;
import com.smilegate.authserver.application.port.in.AccountJwtUseCase;
import com.smilegate.authserver.application.port.in.JwtProviderUseCase;
import com.smilegate.authserver.domain.dto.LoginResponseDto;
import com.smilegate.authserver.domain.user.User;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountJwtService implements AccountJwtUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserPersistenceAdapter userPersistenceAdapter;
    private final JwtProviderUseCase jwtProviderUseCase;

    @Override
    @Transactional
    public LoginResponseDto reIssueAccessToken(String email, String refreshedToken) {
        User user = userPersistenceAdapter.loadUserByUserEmail(email);
        String refreshToken = jwtProviderUseCase.createRefreshToken(email,user.getRole());
        String accessToken = jwtProviderUseCase.createAccessToken(user.getEmail(), user.getRole());
        return new LoginResponseDto(accessToken, refreshToken);
    }


    @Override
    @Transactional
    public LoginResponseDto login(String email, String password) {
        User account = userPersistenceAdapter
                .loadUserByUserEmail(email);
        checkPassword(password, account.getPassword());
        return createToken(account);
    }

    @Override
    public void logout(String refreshToken) {
        jwtProviderUseCase.logout(refreshToken);
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
            throw new BadRequestException("????????? ?????? ??????????????? ???????????????");
        }
    }
}
