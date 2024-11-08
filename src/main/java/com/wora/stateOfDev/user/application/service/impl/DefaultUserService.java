package com.wora.stateOfDev.user.application.service.impl;

import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.user.application.service.UserService;
import com.wora.stateOfDev.user.domain.entity.User;
import com.wora.stateOfDev.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository repository;

    @Override
    public User findByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("user", username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByEmail(username);
    }
}
