package com.smilegate.authserver.adapter.out.persistence;

import com.smilegate.authserver.domain.user.User;

public interface RecordUserPort {
    User save(User user);
}

