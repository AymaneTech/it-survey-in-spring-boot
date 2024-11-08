package com.wora.stateOfDev.owner.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, OwnerId> {
}
