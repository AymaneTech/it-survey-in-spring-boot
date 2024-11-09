package com.wora.stateOfDev.user.application.service;

import com.wora.stateOfDev.user.domain.entity.User;

public interface UserDetailsProvider {
    User findByUsername(String username);

}
