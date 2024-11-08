package com.wora.stateOfDev.user.application.dto.emeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorityEmbeddableDto(@NotNull Long id,
                                     @NotBlank String name,
                                     @NotBlank String value) {
}
