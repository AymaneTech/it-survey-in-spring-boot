package com.wora.stateOfDev.survey.application.dto.embeddable;

import com.wora.stateOfDev.owner.application.dto.OwnerEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyEmbeddableDto(@NotNull Long id,
                                  @NotBlank String title,
                                  @NotBlank String description,
                                  @NotNull OwnerEmbeddableDto owner
) {
}
