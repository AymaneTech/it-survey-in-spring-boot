package com.wora.stateOfDev.user.domain.valueObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Name (@NotBlank String firstName, @NotBlank String lastName){}