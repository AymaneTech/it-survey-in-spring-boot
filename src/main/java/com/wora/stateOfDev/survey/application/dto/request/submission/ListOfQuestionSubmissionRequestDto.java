package com.wora.stateOfDev.survey.application.dto.request.submission;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ListOfQuestionSubmissionRequestDto(@NotNull List<SingleQuestionSubmissionRequestDto> submissions) implements SurveySubmission{
}
