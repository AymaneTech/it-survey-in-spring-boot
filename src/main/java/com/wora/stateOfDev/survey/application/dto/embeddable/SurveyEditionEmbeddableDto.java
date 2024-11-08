package com.wora.stateOfDev.survey.application.dto.embeddable;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.Year;

public record SurveyEditionEmbeddableDto(@NotNull Long id,
                                         @NotNull LocalDateTime startDate,
                                         @NotNull LocalDateTime endDate,
                                         @NotNull Year year,
                                         @NotNull SurveyEmbeddableDto survey
) {
}
