package com.wora.stateOfDev.user.application.service;

import com.wora.stateOfDev.user.application.dto.request.LoginRequestDto;
import com.wora.stateOfDev.user.application.dto.request.RegisterRequestDto;
import com.wora.stateOfDev.user.application.dto.response.AuthenticationResponse;
import com.wora.stateOfDev.user.application.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(RegisterRequestDto dto);

    boolean existsByUsername(String username);

    AuthenticationResponse login(LoginRequestDto dto);
}
