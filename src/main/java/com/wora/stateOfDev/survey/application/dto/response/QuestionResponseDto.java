package com.wora.stateOfDev.survey.application.dto.response;

import com.wora.stateOfDev.survey.application.dto.embeddable.AnswerEmbeddableDto;
import com.wora.stateOfDev.survey.application.dto.embeddable.ChapterEmbeddableDto;
import com.wora.stateOfDev.survey.domain.valueObject.AnswerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionResponseDto(@NotNull Long id,
                                  @NotBlank String text,
                                  @NotNull AnswerType answerType,
                                  @NotNull List<AnswerEmbeddableDto> answers,
                                  @NotNull ChapterEmbeddableDto chapter
) {
}
