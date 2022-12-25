package com.smilegate.authserver.adapter.out.persistence;

import com.smilegate.authserver.adapter.in.web.UserUpdateRequestDto;
import com.smilegate.authserver.application.port.out.LoadUserPort;
import com.smilegate.authserver.application.port.out.RecordUserPort;
import com.smilegate.authserver.domain.user.User;
import com.smilegate.authserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, RecordUserPort {
    private static final String NOT_FOUND_USER_MESSAGE = "존재하지 않는 유저입니다.";
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByNameAndEmail(userUpdateRequestDto.getName(), userUpdateRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER_MESSAGE));
        user.update(userUpdateRequestDto);
        return user;
    }

    @Override
    public boolean deleteUser(UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByNameAndEmail(userUpdateRequestDto.getName(), userUpdateRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER_MESSAGE));
        user.delete(user);
        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User loadByEmal(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER_MESSAGE));
    }

    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER_MESSAGE));
    }

    @Override
    public List<User> loadByName(String userName) {
        return userRepository.findByName(userName);
    }

    @Override
    @Transactional
    public List<User> loadAll(Integer page){
        Pageable pageable = PageRequest.of(page, 20);
        return userRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    public User loadUserByUserEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER_MESSAGE));
    }
}
