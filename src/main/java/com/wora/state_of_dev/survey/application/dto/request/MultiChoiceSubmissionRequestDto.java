package com.wora.state_of_dev.survey.application.dto.request;

import java.util.List;

public record MultiChoiceSubmissionRequestDto(List<Long> answer) implements AnswerSubmissionRequestDto<List<Long>> {
}
