package com.wora.state_of_dev.survey.application.dto.request.submission;

import jakarta.validation.constraints.NotNull;

public record SingleQuestionSubmissionRequestDto(@NotNull Long questionId,
                                                 AnswerSubmissionRequestDto<?> answer) implements SurveySubmission {
}
