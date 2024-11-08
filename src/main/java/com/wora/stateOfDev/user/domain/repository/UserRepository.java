package com.wora.stateOfDev.user.domain.repository;

import com.wora.stateOfDev.user.domain.entity.User;
import com.wora.stateOfDev.user.domain.valueObject.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
