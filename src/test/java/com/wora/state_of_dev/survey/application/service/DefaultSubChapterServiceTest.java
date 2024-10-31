package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.embeddable.ChapterEmbeddableDto;
import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SubChapterResponseDto;
import com.wora.state_of_dev.survey.application.mapper.ChapterMapper;
import com.wora.state_of_dev.survey.application.service.impl.DefaultSubChapterService;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.repository.ChapterRepository;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("Default Sub chapter service tests")
@ExtendWith(MockitoExtension.class)
class DefaultSubChapterServiceTest {
    @Mock
    private ChapterRepository repository;
    @Mock
    private ChapterMapper mapper;
    @InjectMocks
    private DefaultSubChapterService sut;

    private Chapter chapter;
    private Chapter parentChapter;

    @BeforeEach
    void setup() {
        parentChapter = new Chapter("frameworks")
                .setId(new ChapterId(1L));
        chapter = new Chapter("web frameworks")
                .setId(new ChapterId(2L))
                .setParentChapter(parentChapter);
    }

    @Test
    void given_validRequest_whenCreate_shouldCreateAndReturnChapter() {
        ChapterRequestDto requestDto = new ChapterRequestDto("web frameworks");

        given(repository.findById(any(ChapterId.class))).willReturn(Optional.of(parentChapter));
        given(repository.save(any(Chapter.class))).willReturn(chapter);
        given(mapper.toSubChapterResponse(any(Chapter.class)))
                .willAnswer(invocation -> {
                    Chapter arg = invocation.getArgument(0);
                    return new SubChapterResponseDto(arg.getId().value(), arg.getTitle(),
                            new ChapterEmbeddableDto(parentChapter.getId().value(), parentChapter.getTitle(), null));
                });

        SubChapterResponseDto actual = sut.create(new ChapterId(5L), requestDto);

        assertThat(actual).isNotNull();
        assertThat(actual.title()).isEqualTo(requestDto.title());
        verify(repository).save(any(Chapter.class));
    }

    @Test
    void given_surveyEditionDoesNotExist_whenCreate_shouldThrowEntityNotFoundException() {
        ChapterRequestDto requestDto = new ChapterRequestDto("New Chapter");

        given(repository.findById(any(ChapterId.class))).willReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> sut.create(new ChapterId(5L), requestDto))
                .withMessageContaining("parent chapter with id 5 not found");
    }
}