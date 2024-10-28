package com.wora.state_of_dev.survey.application.dto.response;

import com.wora.state_of_dev.survey.application.dto.embeddable.ChapterEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubChapterResponseDto(@NotNull Long id,
                                    @NotBlank String title,
                                    @NotNull ChapterEmbeddableDto parentChapter
) {
}
