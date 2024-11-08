package com.wora.state_of_dev.user.domain.valueObject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record AuthorityId(@GeneratedValue Long value) {
}
