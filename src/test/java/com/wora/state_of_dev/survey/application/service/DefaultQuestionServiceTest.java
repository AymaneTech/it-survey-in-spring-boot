package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.embeddable.AnswerEmbeddableDto;
import com.wora.state_of_dev.survey.application.dto.embeddable.ChapterEmbeddableDto;
import com.wora.state_of_dev.survey.application.dto.request.AnswerRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.QuestionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.QuestionResponseDto;
import com.wora.state_of_dev.survey.application.mapper.AnswerMapper;
import com.wora.state_of_dev.survey.application.mapper.QuestionMapper;
import com.wora.state_of_dev.survey.application.service.impl.DefaultQuestionService;
import com.wora.state_of_dev.survey.domain.entities.Answer;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.entities.Question;
import com.wora.state_of_dev.survey.domain.exception.ChapterHasSubChaptersException;
import com.wora.state_of_dev.survey.domain.repository.ChapterRepository;
import com.wora.state_of_dev.survey.domain.repository.QuestionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("Default question service tests")
@ExtendWith(MockitoExtension.class)
class DefaultQuestionServiceTest {
    @Mock
    private QuestionRepository repository;
    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private QuestionMapper mapper;
    @Mock
    private AnswerMapper answerMapper;
    @InjectMocks
    private DefaultQuestionService sut;

    private Chapter chapter;
    private Question question;
    private QuestionRequestDto dto;

    @BeforeEach
    void setup() {
        chapter = new Chapter("most useed frameworks").setId(new ChapterId(2L));
        question = new Question(1L, "what framework do you use for backend dev", AnswerType.SINGLE_CHOICE, chapter)
                ._setAnswers(List.of(
                        new Answer(1L, "Spring boot"),
                        new Answer(2L, "Laravel"),
                        new Answer(3L, ".NET")
                ));
        dto = new QuestionRequestDto("what is the stack", 2L, AnswerType.SINGLE_CHOICE,
                List.of(
                        new AnswerRequestDto("java"),
                        new AnswerRequestDto("c")
                ));
    }

    private void mockMapToResponseDto() {
        given(mapper.toResponseDto(any(Question.class))).willAnswer(invocation -> {
            Question arg = invocation.getArgument(0);
            List<AnswerEmbeddableDto> answers = arg.getAnswers()
                    .stream().map(a -> new AnswerEmbeddableDto(a.getId().value(), a.getText(), a.getSelectCount()))
                    .toList();

            return new QuestionResponseDto(
                    arg.getId().value(), arg.getText(), arg.getAnswerType(),
                    answers,
                    new ChapterEmbeddableDto(chapter.getId().value(), chapter.getTitle(), null)
            );
        });
    }

    @Nested
    class FindAllTests {
        @Test
        void given_questionsDoesNotExists_whenFindAll_shouldReturnEmptyList() {
            given(repository.findAll()).willReturn(List.of());

            List<QuestionResponseDto> actual = sut.findAll();

            assertThat(actual.size()).isEqualTo(0);
            verify(repository).findAll();
            verify(mapper, never()).toResponseDto(any(Question.class));
        }

        @Test
        void given_questionsExists_whenFindAll_shouldReturnQuestionsList() {
            given(repository.findAll()).willReturn(List.of(question));
            mockMapToResponseDto();

            List<QuestionResponseDto> actual = sut.findAll();

            assertThat(actual.size()).isEqualTo(1);
            assertThat(actual.getFirst().text()).isEqualTo(question.getText());
        }
    }

    @Nested
    class FindById {
        @Test
        void given_questionDoesNotExists_whenFindById_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(QuestionId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.findById(new QuestionId(2L)))
                    .withMessageContaining("question with id 2 not found");
        }

        @Test
        void given_questionExists_whenFindById_shouldReturnQuestion() {
            given(repository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
            mockMapToResponseDto();

            QuestionResponseDto actual = sut.findById(new QuestionId(2L));

            assertThat(actual).isNotNull();
            assertThat(actual.text()).isEqualTo(question.getText());
            verify(repository).findById(any(QuestionId.class));
        }
    }

    @Nested
    class CreateTests {
        @Test
        void given_questionDto_whenCreate_shouldCreateAndReturnQuestion() {
            given(chapterRepository.findById(new ChapterId(2L))).willReturn(Optional.of(chapter));
            given(mapper.toEntity(eq(dto))).willReturn(question);
            given(answerMapper.toEntity(any(AnswerRequestDto.class))).willAnswer(invocation -> new Answer(1L, "example"));
            given(repository.save(any(Question.class))).willReturn(question);
            mockMapToResponseDto();

            QuestionResponseDto actual = sut.create(dto);

            assertThat(actual).isNotNull();
            verify(repository).save(any(Question.class));
        }

        @Test
        void given_chapterIdDoesNotExist_whenCreate_shouldThrowEntityNotFoundException() {
            given(chapterRepository.findById(any(ChapterId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.create(dto))
                    .withMessageContaining("chapter with id 2 not found");
        }

        @Test
        void givenChapterHasSubChapters_whenCreate_shouldThrowChapterHasSubChaptersException() {
            given(chapterRepository.findById(new ChapterId(2L))).willReturn(Optional.of(chapter));
            given(chapterRepository.isHasSubChapters(any(ChapterId.class))).willReturn(true);

            assertThatExceptionOfType(ChapterHasSubChaptersException.class)
                    .isThrownBy(() -> sut.create(dto))
                    .withMessageContaining("Cannot create question with chapter that has sub chapters");
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void given_questionNotExists_whenUpdate_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(QuestionId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new QuestionId(2L), dto))
                    .withMessageContaining("question with id 2 not found");
        }

        @Test
        void given_chapterNotExists_whenUpdate_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
            given(chapterRepository.findById(new ChapterId(2L))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new QuestionId(3L), dto))
                    .withMessageContaining("chapter with id 2 not found");
        }

        @Test
        void given_questionDto_whenUpdate_shouldUpdateAndReturnQuestion() {
            given(repository.findById(question.getId())).willReturn(Optional.of(question));
            given(chapterRepository.findById(eq(chapter.getId()))).willReturn(Optional.of(chapter));
            given(answerMapper.toEntity(any(AnswerRequestDto.class))).willAnswer(invocation -> new Answer(1L, "example"));
            mockMapToResponseDto();

            QuestionResponseDto actual = sut.update(question.getId(), dto);

            assertThat(actual).isNotNull();
            assertThat(actual.text()).isEqualTo("what is the stack");
            verify(repository).findById(any(QuestionId.class));
        }

        @Test
        void givenChapterHasSubChapters_whenUpdate_shouldThrowChapterHasSubChaptersException() {
            given(repository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
            given(chapterRepository.isHasSubChapters(any(ChapterId.class))).willReturn(true);

            assertThatExceptionOfType(ChapterHasSubChaptersException.class)
                    .isThrownBy(() -> sut.update(new QuestionId(3L), dto))
                    .withMessageContaining("Cannot create question with chapter that has sub chapters");
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void given_questionIdNotExists_whenDelete_shouldThrowEntityNotFoundException() {
            given(repository.existsById(any(QuestionId.class))).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.delete(new QuestionId(2L)))
                    .withMessageContaining("question with id 2 not found");
        }

        @Test
        void given_questionExists_whenDelete_shouldDeleteQuestion() {
            given(repository.existsById(any(QuestionId.class))).willReturn(true);

            sut.delete(new QuestionId(2L));

            verify(repository).deleteById(any(QuestionId.class));
        }
    }
}