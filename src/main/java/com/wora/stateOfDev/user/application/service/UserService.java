package com.wora.stateOfDev.user.application.service;

import com.wora.stateOfDev.user.application.dto.request.RegisterRequestDto;
import com.wora.stateOfDev.user.application.dto.response.UserResponseDto;
import com.wora.stateOfDev.user.domain.entity.User;

public interface UserService {
    UserResponseDto register(RegisterRequestDto dto);

    User findByUsername(String username);

    boolean existsByUsername(String username);
}
