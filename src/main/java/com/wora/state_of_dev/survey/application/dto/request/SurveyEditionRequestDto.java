package com.wora.state_of_dev.survey.application.dto.request;

import com.wora.state_of_dev.common.application.validation.ReferenceExists;
import com.wora.state_of_dev.survey.domain.entities.Survey;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.Year;

public record SurveyEditionRequestDto(@NotNull @FutureOrPresent LocalDateTime startDate,
                                      @NotNull @Future LocalDateTime endDate,
                                      @NotNull @FutureOrPresent Year year,
                                      @NotNull
                                      @ReferenceExists(entityClass = Survey.class, idClass = SurveyId.class, message = "no parent chapter with this id")
                                      Long surveyId
) {
}
