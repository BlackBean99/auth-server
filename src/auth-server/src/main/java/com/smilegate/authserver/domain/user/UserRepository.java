package com.smilegate.authserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsAccountByUserEmail(String email);
}
