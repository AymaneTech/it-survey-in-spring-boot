package com.wora.stateOfDev.survey.application.dto.embeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChapterEmbeddableDto(@NotNull Long id,
                                   @NotBlank String title,
                                   @NotNull SurveyEditionEmbeddableDto surveyEdition
) {
}
