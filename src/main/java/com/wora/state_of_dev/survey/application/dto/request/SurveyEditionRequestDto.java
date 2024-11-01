package com.wora.state_of_dev.survey.application.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.Year;

public record SurveyEditionRequestDto(@NotNull @FutureOrPresent LocalDateTime startDate,
                                      @NotNull @Future LocalDateTime endDate,
                                      @NotNull @FutureOrPresent Year year,
                                      @NotNull Long surveyId
) {
}
