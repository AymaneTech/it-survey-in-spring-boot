package com.wora.state_of_dev.survey.application.dto.request.submission;

import java.util.List;

public record MultiChoiceSubmissionRequestDto(List<Long> answer) implements AnswerSubmissionRequestDto<List<Long>> {
}
