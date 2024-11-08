package com.wora.stateOfDev.survey.application.dto.request.submission;

public sealed interface AnswerSubmissionRequestDto<R> permits MultiChoiceSubmissionRequestDto, SingleChoiceSubmissionRequestDto {
    R answer();
}