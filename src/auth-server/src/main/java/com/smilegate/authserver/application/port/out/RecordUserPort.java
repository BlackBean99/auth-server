package com.smilegate.authserver.application.port.out;

import com.smilegate.authserver.domain.user.User;

public interface RecordUserPort {
    User save(User account);
}
