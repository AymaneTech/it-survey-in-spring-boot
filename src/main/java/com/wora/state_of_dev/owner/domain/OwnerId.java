package com.wora.state_of_dev.owner.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Embeddable
public record OwnerId(@GeneratedValue(strategy = GenerationType.SEQUENCE) Long value) {
}
