package com.wora.state_of_dev.user.domain.repository;

import com.wora.state_of_dev.user.domain.entity.Authority;
import com.wora.state_of_dev.user.domain.valueObject.AuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId>{
}
