package com.wora.state_of_dev.survey.application.dto.request;

import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionRequestDto(@NotBlank String text,
                                 @NotNull Long chapterId,
                                 @NotNull AnswerType answerType,
                                 @NotNull List<AnswerRequestDto> answers
) {
}
