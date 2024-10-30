package com.wora.state_of_dev.survey.application.dto.embeddable;

import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionEmbeddableDto(@NotNull Long id,
                                    @NotBlank String text,
                                    @NotNull AnswerType answerType,
                                    @NotNull ChapterEmbeddableDto chapter
) {
}
