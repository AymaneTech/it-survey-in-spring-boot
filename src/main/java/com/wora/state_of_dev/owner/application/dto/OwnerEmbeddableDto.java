package com.wora.state_of_dev.owner.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OwnerEmbeddableDto(
        @NotNull Long id,
        @NotBlank String name
) {
}
