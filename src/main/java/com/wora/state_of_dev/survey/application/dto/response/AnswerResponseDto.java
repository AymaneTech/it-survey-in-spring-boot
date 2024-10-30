package com.wora.state_of_dev.survey.application.dto.response;

import com.wora.state_of_dev.survey.application.dto.embeddable.QuestionEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerResponseDto(@NotNull Long id,
                                @NotBlank String text,
                                @NotNull QuestionEmbeddableDto question
) {
}
