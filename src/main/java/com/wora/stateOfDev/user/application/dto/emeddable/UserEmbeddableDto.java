package com.wora.stateOfDev.user.application.dto.emeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserEmbeddableDto(@NotNull Long id,
                                @NotBlank String firstName,
                                @NotBlank String lastName,
                                @NotBlank String email,
                                RoleEmbeddableDto role) {
}
