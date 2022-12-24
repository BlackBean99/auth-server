package com.smilegate.authserver.adapter.out.persistence;

public interface LoadUserPort {
    boolean existsAccountByUserEmail(String email);
}
