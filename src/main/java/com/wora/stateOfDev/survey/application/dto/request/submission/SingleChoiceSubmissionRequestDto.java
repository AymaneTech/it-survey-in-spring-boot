package com.wora.stateOfDev.survey.application.dto.request.submission;

public record SingleChoiceSubmissionRequestDto(Long answer) implements AnswerSubmissionRequestDto<Long> {
}
