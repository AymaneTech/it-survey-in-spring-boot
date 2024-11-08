package com.wora.stateOfDev.user.application.service.impl;

import com.wora.stateOfDev.user.application.dto.request.RegisterRequestDto;
import com.wora.stateOfDev.user.application.dto.response.UserResponseDto;
import com.wora.stateOfDev.user.application.mapper.AuthenticationMapper;
import com.wora.stateOfDev.user.application.service.AuthenticationService;
import com.wora.stateOfDev.user.domain.entity.Role;
import com.wora.stateOfDev.user.domain.entity.User;
import com.wora.stateOfDev.user.domain.repository.RoleRepository;
import com.wora.stateOfDev.user.domain.repository.UserRepository;
import com.wora.stateOfDev.user.domain.valueObject.RoleId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.wora.stateOfDev.common.util.OptionalWrapper.orElseThrow;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public UserResponseDto register(RegisterRequestDto dto) {
        Role role = orElseThrow(roleRepository.findById(new RoleId(dto.roleId())), "role", dto.roleId());

        final User user = mapper.toEntity(dto)
                .setRole(role)
                .setPassword(encoder.encode(dto.password()));

        User savedUser = userRepository.save(user);

        return mapper.toResponseDto(user);
    }
}
