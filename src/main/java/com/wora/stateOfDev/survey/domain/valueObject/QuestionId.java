package com.wora.stateOfDev.survey.domain.valueObject;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public record QuestionId(@GeneratedValue(strategy = GenerationType.SEQUENCE) Long value) {
}
