package com.wora.stateOfDev.survey.application.dto.response;

import com.wora.stateOfDev.survey.application.dto.embeddable.SurveyEmbeddableDto;
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
