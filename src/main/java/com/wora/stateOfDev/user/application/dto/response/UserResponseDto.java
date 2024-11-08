package com.wora.stateOfDev.user.application.dto.response;

import com.wora.stateOfDev.user.application.dto.emeddable.RoleEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResponseDto(@NotNull Long id,
                              @NotBlank String firstName,
                              @NotBlank String lastName,
                              @NotBlank String email,
                              RoleEmbeddableDto role
) {
}
