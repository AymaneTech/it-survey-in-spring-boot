package com.wora.stateOfDev.user.domain.repository;

import com.wora.stateOfDev.user.domain.entity.Authority;
import com.wora.stateOfDev.user.domain.valueObject.AuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId>{
}
