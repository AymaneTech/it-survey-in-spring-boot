package com.wora.stateOfDev.survey.application.dto.response;

import com.wora.stateOfDev.survey.application.dto.embeddable.QuestionEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerResponseDto(@NotNull Long id,
                                @NotBlank String text,
                                @NotNull QuestionEmbeddableDto question
) {
}
