package com.wora.state_of_dev.survey.application.dto.request;

import com.wora.state_of_dev.common.application.validation.ReferenceExists;
import com.wora.state_of_dev.survey.domain.entity.Chapter;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionRequestDto(@NotBlank String text,
                                 @NotNull @ReferenceExists(entityClass = Chapter.class, idClass = ChapterId.class, message = "no chapter with this id")
                                 Long chapterId,
                                 @NotNull AnswerType answerType,
                                 @NotNull List<AnswerRequestDto> answers
) {
}
