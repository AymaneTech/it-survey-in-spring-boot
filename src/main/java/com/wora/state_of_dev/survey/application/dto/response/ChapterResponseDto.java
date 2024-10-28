package com.wora.state_of_dev.survey.application.dto.response;

import com.wora.state_of_dev.survey.application.dto.embeddable.SurveyEditionEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChapterResponseDto(@NotNull Long id,
                                 @NotBlank String title,
                                 @NotNull SurveyEditionEmbeddableDto surveyEdition
) {
}
