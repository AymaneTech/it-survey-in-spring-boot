package com.wora.state_of_dev.survey.application.dto.embeddable;

import com.wora.state_of_dev.owner.application.dto.OwnerEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyEmbeddableDto(@NotBlank String title,
                                  @NotBlank String description,
                                  @NotNull OwnerEmbeddableDto owner
) {
}
