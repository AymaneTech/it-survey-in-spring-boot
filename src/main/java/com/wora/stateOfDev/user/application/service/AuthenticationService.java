package com.wora.stateOfDev.user.application.service;

import com.wora.stateOfDev.user.application.dto.request.RegisterRequestDto;
import com.wora.stateOfDev.user.application.dto.response.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto register(RegisterRequestDto dto);
}
