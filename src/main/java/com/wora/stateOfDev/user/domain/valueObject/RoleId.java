package com.wora.stateOfDev.user.domain.valueObject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record RoleId(@GeneratedValue Long value) {
}
