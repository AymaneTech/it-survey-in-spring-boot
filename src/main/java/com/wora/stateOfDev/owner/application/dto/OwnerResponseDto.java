package com.wora.stateOfDev.owner.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OwnerResponseDto(@NotNull Long id,
                               @NotBlank String name
) {
}
