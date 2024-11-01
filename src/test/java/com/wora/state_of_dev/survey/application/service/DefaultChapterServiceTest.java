package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityCreationException;
import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.embeddable.SurveyEditionEmbeddableDto;
import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.application.mapper.ChapterMapper;
import com.wora.state_of_dev.survey.application.service.impl.DefaultChapterService;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.entities.SurveyEdition;
import com.wora.state_of_dev.survey.domain.repository.ChapterRepository;
import com.wora.state_of_dev.survey.domain.repository.SurveyEditionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Default chapter service tests")
@ExtendWith(MockitoExtension.class)
class DefaultChapterServiceTest {
    @Mock
    private ChapterRepository repository;
    @Mock
    private SurveyEditionRepository surveyEditionRepository;
    @Mock
    private ChapterMapper mapper;
    @InjectMocks
    private DefaultChapterService sut;

    private Chapter chapter;
    private SurveyEdition surveyEdition;

    @BeforeEach
    void setup() {
        surveyEdition = new SurveyEdition(LocalDateTime.now(), LocalDateTime.now().plusDays(4), Year.of(2024))
                .setId(new SurveyEditionId(5L));
        chapter = new Chapter("Most used frameworks")
                .setId(new ChapterId(4L))
                .setSurveyEdition(surveyEdition);
    }


    @Nested
    class FindAllBySurveyEditionIdTests {
        @Test
        void given_chaptersExist_whenFindAllBySurveyEditionId_shouldReturnChaptersList() {
            Chapter chapter2 = new Chapter("Programming languages")
                    .setId(new ChapterId(2L))
                    .setSurveyEdition(surveyEdition);
            List<Chapter> expected = List.of(chapter, chapter2);

            given(surveyEditionRepository.existsById(any(SurveyEditionId.class))).willReturn(true);
            given(repository.findAllBySurveyEditionId(any(SurveyEditionId.class))).willReturn(expected);
            given(mapper.toChapterResponse(any(Chapter.class)))
                    .willAnswer(invocation -> {
                        Chapter arg = invocation.getArgument(0);
                        SurveyEditionEmbeddableDto surveyEditionDto = new SurveyEditionEmbeddableDto(
                                arg.getSurveyEdition().getId().value(),
                                arg.getSurveyEdition().getStartDate(),
                                arg.getSurveyEdition().getEndDate(),
                                arg.getSurveyEdition().getYear(), null
                        );
                        return new ChapterResponseDto(arg.getId().value(), arg.getTitle(), surveyEditionDto);
                    });

            List<ChapterResponseDto> actual = sut.findAllBySurveyEditionId(new SurveyEditionId(5L));

            assertThat(actual).hasSize(2);
            assertThat(actual).isNotNull();
            verify(repository).findAllBySurveyEditionId(any(SurveyEditionId.class));
            verify(mapper, times(2)).toChapterResponse(any(Chapter.class));
        }

        @Test
        void given_noChaptersExist_whenFindAllBySurveyEditionId_shouldReturnEmptyList() {
            given(surveyEditionRepository.existsById(any(SurveyEditionId.class))).willReturn(true);
            given(repository.findAllBySurveyEditionId(any(SurveyEditionId.class))).willReturn(List.of());

            List<ChapterResponseDto> actual = sut.findAllBySurveyEditionId(new SurveyEditionId(5L));

            assertThat(actual).isEmpty();
            verify(repository).findAllBySurveyEditionId(any(SurveyEditionId.class));
            verify(mapper, never()).toChapterResponse(any(Chapter.class));
        }

        @Test
        void givenInvalidSurveyEditionId_whenFindAllBySurveyEditionId_shouldThrowEntityNotFoundException() {
            given(surveyEditionRepository.existsById(any(SurveyEditionId.class))).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.findAllBySurveyEditionId(new SurveyEditionId(1L)))
                    .withMessageContaining("survey edition with id 1 not found");
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void given_chapterExists_whenFindById_shouldReturnChapter() {
            given(repository.findById(any(ChapterId.class))).willReturn(Optional.of(chapter));
            given(mapper.toChapterResponse(any(Chapter.class)))
                    .willAnswer(invocation -> {
                        Chapter arg = invocation.getArgument(0);
                        SurveyEditionEmbeddableDto surveyEditionDto = new SurveyEditionEmbeddableDto(
                                arg.getSurveyEdition().getId().value(),
                                arg.getSurveyEdition().getStartDate(),
                                arg.getSurveyEdition().getEndDate(),
                                arg.getSurveyEdition().getYear(), null
                        );
                        return new ChapterResponseDto(arg.getId().value(), arg.getTitle(), surveyEditionDto);
                    });

            ChapterResponseDto actual = sut.findById(new ChapterId(1L));

            assertThat(actual.title()).isEqualTo(chapter.getTitle());
            verify(repository).findById(any(ChapterId.class));
            verify(mapper).toChapterResponse(any(Chapter.class));
        }

        @Test
        void given_chapterDoesNotExist_whenFindById_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(ChapterId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.findById(new ChapterId(1L)))
                    .withMessageContaining("chapter with id 1 not found");
        }
    }

    @Nested
    class CreateTests {
        @Test
        void given_validRequest_whenCreate_shouldCreateAndReturnChapter() {
            ChapterRequestDto requestDto = new ChapterRequestDto("Most used frameworks");

            given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
            given(repository.existsByTitleAndSurveyEditionId(eq(requestDto.title()), any(SurveyEditionId.class))).willReturn(false);
            given(mapper.toEntity(eq(requestDto))).willReturn(chapter);
            given(repository.save(any(Chapter.class))).willReturn(chapter);
            given(mapper.toChapterResponse(any(Chapter.class)))
                    .willAnswer(invocation -> {
                        Chapter arg = invocation.getArgument(0);
                        SurveyEditionEmbeddableDto surveyEditionDto = new SurveyEditionEmbeddableDto(
                                arg.getSurveyEdition().getId().value(),
                                arg.getSurveyEdition().getStartDate(),
                                arg.getSurveyEdition().getEndDate(),
                                arg.getSurveyEdition().getYear(), null
                        );
                        return new ChapterResponseDto(arg.getId().value(), arg.getTitle(), surveyEditionDto);
                    });

            ChapterResponseDto actual = sut.create(new SurveyEditionId(5L), requestDto);

            assertThat(actual).isNotNull();
            assertThat(actual.title()).isEqualTo(requestDto.title());
            verify(surveyEditionRepository).findById(any(SurveyEditionId.class));
            verify(repository).save(any(Chapter.class));
        }

        @Test
        void given_surveyEditionDoesNotExist_whenCreate_shouldThrowEntityNotFoundException() {
            ChapterRequestDto requestDto = new ChapterRequestDto("New Chapter");

            given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.create(new SurveyEditionId(5L), requestDto))
                    .withMessageContaining("survey edition with id 5 not found");
        }

        @Test
        void given_duplicateChapterTitle_whenCreate_shouldThrowEntityCreationException() {
            ChapterRequestDto requestDto = new ChapterRequestDto("Existing Chapter");

            given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
            given(repository.existsByTitleAndSurveyEditionId(eq(requestDto.title()), any(SurveyEditionId.class))).willReturn(true);

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> sut.create(new SurveyEditionId(5L), requestDto))
                    .withMessageContaining("Failed to save the chapter because chapter name already used in this survey edition");
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void given_chapterExists_whenUpdate_shouldUpdateAndReturnChapter() {
            ChapterRequestDto requestDto = new ChapterRequestDto("Updated Chapter Title");

            given(repository.findById(any(ChapterId.class))).willReturn(Optional.of(chapter));
            given(mapper.toChapterResponse(any(Chapter.class)))
                    .willAnswer(invocation -> {
                        Chapter arg = invocation.getArgument(0);
                        SurveyEditionEmbeddableDto surveyEditionDto = new SurveyEditionEmbeddableDto(
                                arg.getSurveyEdition().getId().value(),
                                arg.getSurveyEdition().getStartDate(),
                                arg.getSurveyEdition().getEndDate(),
                                arg.getSurveyEdition().getYear(), null
                        );
                        return new ChapterResponseDto(arg.getId().value(), arg.getTitle(), surveyEditionDto);
                    });

            ChapterResponseDto actual = sut.update(new ChapterId(1L), requestDto);

            assertThat(actual).isNotNull();
            assertThat(actual.title()).isEqualTo(requestDto.title());
            verify(repository).findById(any(ChapterId.class));
        }

        @Test
        void given_chapterDoesNotExist_whenUpdate_shouldThrowEntityNotFoundException() {
            ChapterRequestDto requestDto = new ChapterRequestDto("Updated Chapter Title");

            given(repository.findById(any(ChapterId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new ChapterId(1L), requestDto))
                    .withMessageContaining("chapter  with id 1 not found");
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void given_chapterDoesNotExist_whenDelete_shouldThrowEntityNotFoundException() {
            given(repository.existsById(any(ChapterId.class))).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.delete(new ChapterId(1L)))
                    .withMessageContaining("chapter with id 1 not found");
        }

        @Test
        void given_chapterExists_whenDelete_shouldDeleteChapter() {
            given(repository.existsById(any(ChapterId.class))).willReturn(true);

            sut.delete(new ChapterId(1L));

            verify(repository).existsById(any(ChapterId.class));
            verify(repository).deleteById(any(ChapterId.class));
        }
    }
}