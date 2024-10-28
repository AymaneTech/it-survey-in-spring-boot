package com.wora.state_of_dev.survey.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.Year;

public record SurveyEditionRequestDto(@NotNull LocalDateTime startDate,
                                      @NotNull LocalDateTime endDate,
                                      @NotNull Year year,
                                      @NotNull Long surveyId
) {
}
