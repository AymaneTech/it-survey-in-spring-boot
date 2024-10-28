package com.wora.state_of_dev.owner.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, OwnerId> {
}
