package com.wora.stateOfDev.survey.application.dto.request.submission;

import java.util.List;

public record MultiChoiceSubmissionRequestDto(List<Long> answer) implements AnswerSubmissionRequestDto<List<Long>> {
}
