package com.smilegate.authserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String userEmail);

    List<User> findByName(String userName);

    Optional<User> findByNameAndEmail(String name, String email);
}
