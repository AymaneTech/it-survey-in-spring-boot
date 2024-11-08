package com.wora.stateOfDev.user.application.service;

import com.wora.stateOfDev.user.domain.entity.User;

public interface UserService {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
