package com.wora.stateOfDev.user.application.dto.emeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleEmbeddableDto(@NotNull Long id,
                                @NotBlank String name,
                                List<AuthorityEmbeddableDto> authorities) {
}
