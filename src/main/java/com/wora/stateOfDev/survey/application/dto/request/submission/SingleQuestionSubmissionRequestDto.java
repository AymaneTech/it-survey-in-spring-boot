package com.wora.stateOfDev.survey.application.dto.request.submission;

import jakarta.validation.constraints.NotNull;

public record SingleQuestionSubmissionRequestDto(@NotNull Long questionId,
                                                 AnswerSubmissionRequestDto<?> answer) implements SurveySubmission {
}
