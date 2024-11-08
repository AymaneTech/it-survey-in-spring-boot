package com.wora.stateOfDev.user.domain.repository;

import com.wora.stateOfDev.user.domain.entity.Role;
import com.wora.stateOfDev.user.domain.valueObject.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}