package com.wora.stateOfDev.survey.application.dto.request;

import com.wora.stateOfDev.common.application.validation.ReferenceExists;
import com.wora.stateOfDev.survey.domain.entity.Chapter;
import com.wora.stateOfDev.survey.domain.valueObject.AnswerType;
import com.wora.stateOfDev.survey.domain.valueObject.ChapterId;
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
