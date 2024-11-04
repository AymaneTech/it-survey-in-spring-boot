package com.wora.state_of_dev.survey.application.dto.request.submission;

public record SingleChoiceSubmissionRequestDto(Long answer) implements AnswerSubmissionRequestDto<Long> {
}
