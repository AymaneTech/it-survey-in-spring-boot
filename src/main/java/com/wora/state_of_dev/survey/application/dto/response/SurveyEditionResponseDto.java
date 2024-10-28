package com.wora.state_of_dev.survey.application.dto.response;

import com.wora.state_of_dev.survey.application.dto.embeddable.SurveyEmbeddableDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.Year;

public record SurveyEditionResponseDto(@NotNull Long id,
                                       @NotNull LocalDateTime startDate,
                                       @NotNull LocalDateTime endDate,
                                       @NotNull Year year,
                                       @NotNull SurveyEmbeddableDto survey
) {
}
