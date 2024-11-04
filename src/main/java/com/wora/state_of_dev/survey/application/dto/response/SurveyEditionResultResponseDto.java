package com.wora.state_of_dev.survey.application.dto.response;

import com.wora.state_of_dev.survey.application.dto.embeddable.AnswerEmbeddableDto;

import java.time.Year;
import java.util.List;

public record SurveyEditionResultResponseDto(Long id,
                                             String title,
                                             String description,
                                             Year year,
                                             List<NestedChapter> chapters
) {
    public record NestedChapter(Long id,
                                String title,
                                List<NestedChapter> subChapters,
                                List<NestedQuestion> questions
    ) {
    }

    public record NestedQuestion(Long id,
                                 String text,
                                 List<AnswerEmbeddableDto> answers,
                                 int answerCount
    ) {
    }
}
