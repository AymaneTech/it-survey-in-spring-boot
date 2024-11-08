package com.wora.state_of_dev.user.domain.valueObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Name (@NotBlank String firstName, @NotBlank String lastName){}