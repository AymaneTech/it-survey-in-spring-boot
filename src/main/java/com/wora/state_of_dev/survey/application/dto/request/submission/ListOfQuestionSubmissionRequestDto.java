package com.wora.state_of_dev.survey.application.dto.request.submission;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ListOfQuestionSubmissionRequestDto(@NotNull List<SingleQuestionSubmissionRequestDto> submissions) implements SurveySubmission{
}
