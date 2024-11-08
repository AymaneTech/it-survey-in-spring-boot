package com.wora.stateOfDev.survey.application.dto.embeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerEmbeddableDto(@NotNull Long id,
                                  @NotBlank String text,
                                  @NotNull int selectCount
) {
}
