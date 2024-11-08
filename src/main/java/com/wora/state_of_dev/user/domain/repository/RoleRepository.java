package com.wora.state_of_dev.user.domain.repository;

import com.wora.state_of_dev.user.domain.entity.Role;
import com.wora.state_of_dev.user.domain.valueObject.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
