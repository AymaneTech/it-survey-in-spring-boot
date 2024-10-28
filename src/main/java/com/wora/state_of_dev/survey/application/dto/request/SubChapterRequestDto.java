package com.wora.state_of_dev.survey.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubChapterRequestDto(@NotBlank String title,
                                   @NotNull Long parentId
) {
}
