package com.wora.state_of_dev.survey.application.dto.request;

public record SingleChoiceSubmissionRequestDto(Long answer) implements AnswerSubmissionRequestDto<Long> {
}
