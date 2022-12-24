package com.smilegate.authserver.adapter.out.persistence;

public interface LoadUserPort {
    boolean existsUserByUserEmail(String email);
}
