package com.wora.state_of_dev.survey.application.dto.request;

public sealed interface AnswerSubmissionRequestDto<R> permits MultiChoiceSubmissionRequestDto, SingleChoiceSubmissionRequestDto {
    R answer();
}