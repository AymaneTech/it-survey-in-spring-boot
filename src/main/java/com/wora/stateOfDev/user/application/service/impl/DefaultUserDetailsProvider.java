package com.wora.stateOfDev.user.application.service.impl;

import com.wora.stateOfDev.user.application.service.UserDetailsProvider;
import com.wora.stateOfDev.user.domain.entity.User;
import com.wora.stateOfDev.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.wora.stateOfDev.common.util.OptionalWrapper.orElseThrow;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsProvider implements UserDetailsProvider {
    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return orElseThrow(userRepository.findByEmail(username), "Email already taken");
    }
}
