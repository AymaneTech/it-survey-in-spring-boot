package com.wora.state_of_dev.owner.application.dto;

import jakarta.validation.constraints.NotBlank;

public record OwnerRequestDto(@NotBlank String name) {
}
