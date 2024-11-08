package com.wora.stateOfDev.survey.application.dto.response;

import com.wora.stateOfDev.survey.application.dto.embeddable.SurveyEditionEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChapterResponseDto(@NotNull Long id,
                                 @NotBlank String title,
                                 @NotNull SurveyEditionEmbeddableDto surveyEdition
) {
}
