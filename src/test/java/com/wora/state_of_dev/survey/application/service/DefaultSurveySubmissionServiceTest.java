package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.submission.ListOfQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.submission.MultiChoiceSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.submission.SingleChoiceSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.submission.SingleQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.service.impl.DefaultSurveySubmissionService;
import com.wora.state_of_dev.survey.domain.entity.Answer;
import com.wora.state_of_dev.survey.domain.entity.Chapter;
import com.wora.state_of_dev.survey.domain.entity.Question;
import com.wora.state_of_dev.survey.domain.entity.SurveyEdition;
import com.wora.state_of_dev.survey.domain.exception.GivenAnswerNotBelongToQuestion;
import com.wora.state_of_dev.survey.domain.exception.QuestionNotBelongToSurveyEdition;
import com.wora.state_of_dev.survey.domain.exception.SurveyEditionNotOpenedNow;
import com.wora.state_of_dev.survey.domain.repository.AnswerRepository;
import com.wora.state_of_dev.survey.domain.repository.QuestionRepository;
import com.wora.state_of_dev.survey.domain.repository.SurveyEditionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Default survey submission service test")
@ExtendWith(MockitoExtension.class)
class DefaultSurveySubmissionServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private SurveyEditionRepository surveyEditionRepository;
    @InjectMocks
    private DefaultSurveySubmissionService sut;

    private SurveyEdition surveyEdition;
    private Chapter chapter;
    private Question question;

    @BeforeEach
    void setup() {
        surveyEdition = new SurveyEdition(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(9), Year.now())
                .setId(new SurveyEditionId(23L));
        chapter = new Chapter("profile")
                .setSurveyEdition(surveyEdition)
                .setId(new ChapterId(3L));
        question = new Question(3L, "what is you stack", AnswerType.SINGLE_CHOICE, chapter);
    }

    @Test
    void givenSurveyEditionIdNotFound_whenSubmit_shouldThrowEntityNotFound() {
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(1L));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> sut.submit(new SurveyEditionId(2L), dto))
                .withMessageContaining("survey edition with id 2 not found");
    }

    @Test
    void givenParticipationTimeIsNotBetweenStartAndEndDateOfSurveyEdition_whenSubmit_shouldThrowSurveyEditionNotOpenedNow() {
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(1L));
        surveyEdition.setStartDate(LocalDateTime.now().plusDays(9));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));

        assertThatExceptionOfType(SurveyEditionNotOpenedNow.class)
                .isThrownBy(() -> sut.submit(surveyEdition.getId(), dto))
                .withMessageContaining("you are trying to participate in a closed survey edition");
    }

    @Test
    void givenInvalidQuestionId_whenSubmit_shouldThrowEntityNotFoundException() {
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(1L));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> sut.submit(new SurveyEditionId(2L), dto))
                .withMessageContaining("question with id 1 not found");
    }

    @Test
    void givenQuestionNotBelongToSurveyEdition_whenSubmit_shouldThrowQuestionNotBelongToSurveyEdition() {
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(1L));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.of(question));

        assertThatExceptionOfType(QuestionNotBelongToSurveyEdition.class)
                .isThrownBy(() -> sut.submit(new SurveyEditionId(332L), dto))
                .withMessageContaining("the question with ID: 3 does not belong to survey of id 332");
    }

    @Test
    void given__$$$$$$$$$$$_whenSubmit_shouldThrowIllegalArgumentException() {
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new MultiChoiceSubmissionRequestDto(List.of(2L, 4L, 5L)));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.of(question));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sut.submit(surveyEdition.getId(), dto))
                .withMessageContaining("Unsupported answer type");
    }

    @Test
    void givenSingleAnswerAndNotFound_whenSubmit_ShouldThrowEntityNotFoundException() {
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(2L));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
        given(answerRepository.findById(any(AnswerId.class))).willReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> sut.submit(surveyEdition.getId(), dto))
                .withMessageContaining("answer with id 2 not found");
    }

    @Test
    void givenSingleAnswerNotBelongToQuestion_whenSubmit_shouldThrowGivenAnswerNotBelongToQuestion() {
        Answer answer = new Answer(8L, "java/angular");
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(2L));
        Question question1 = new Question(2L, "what is the stack", AnswerType.SINGLE_CHOICE, chapter)
                ._setAnswers(List.of(answer));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
        given(answerRepository.findById(any(AnswerId.class))).willReturn(Optional.of(answer));

        assertThatExceptionOfType(GivenAnswerNotBelongToQuestion.class)
                .isThrownBy(() -> sut.submit(surveyEdition.getId(), dto))
                .withMessageContaining("answer with id 8 does not related to question 3");
    }

    @Test
    void givenCorrectQuestionAndAnswerIds_whenSubmit_shouldIncrementAnswerCountAndSelectionCount() {
        Answer answer = new Answer(8L, "java/angular");
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new SingleChoiceSubmissionRequestDto(2L));
        question._setAnswers(List.of(answer));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
        given(answerRepository.findById(any(AnswerId.class))).willReturn(Optional.of(answer));

        sut.submit(surveyEdition.getId(), dto);

        assertThat(answer.getSelectCount()).isEqualTo(1);
        assertThat(question.getAnswerCount()).isEqualTo(1);
    }

    @Test
    void givenCorrectQuestionAndListOfAnswerIds_whenSubmit_shouldIncrementAnswerCountAndSelectionCount() {
        List<Answer> answers = List.of(
                new Answer(8L, "java/angular"),
                new Answer(1L, "java/react"),
                new Answer(9L, "laravel/blade")
        );
        SingleQuestionSubmissionRequestDto dto = new SingleQuestionSubmissionRequestDto(1L, new MultiChoiceSubmissionRequestDto(List.of(2L, 3L, 4L)));
        question.setAnswerType(AnswerType.MULTI_CHOICE);
        question._setAnswers(answers);

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willReturn(Optional.of(question));
        given(answerRepository.findAllById(any(List.class))).willReturn(answers);

        sut.submit(surveyEdition.getId(), dto);

        assertThat(answers.getFirst().getSelectCount()).isEqualTo(1);
        assertThat(question.getAnswerCount()).isEqualTo(1);
    }

    @Test
    void givenListOfQuestionSubmission_whenSubmit_shouldIncrementAnswerCountAndSelectionCount() {
        Question question1 = new Question(4L, "what is you age", AnswerType.MULTI_CHOICE, chapter);
        List<Answer> answers1 = List.of(
                new Answer(8L, "java/angular"),
                new Answer(1L, "java/react"),
                new Answer(9L, "laravel/blade")
        );
        List<Answer> answers2 = List.of(
                new Answer(11L, "java/react"),
                new Answer(23L, "laravel/blade")
        );
        question.setAnswerType(AnswerType.MULTI_CHOICE);
        question._setAnswers(answers1);
        question1._setAnswers(answers2);

        ListOfQuestionSubmissionRequestDto dto = new ListOfQuestionSubmissionRequestDto(List.of(
                new SingleQuestionSubmissionRequestDto(1L, new MultiChoiceSubmissionRequestDto(List.of(2L, 3L, 4L))),
                new SingleQuestionSubmissionRequestDto(4L, new MultiChoiceSubmissionRequestDto(List.of(6L, 7L)))
        ));

        given(surveyEditionRepository.findById(any(SurveyEditionId.class))).willReturn(Optional.of(surveyEdition));
        given(questionRepository.findById(any(QuestionId.class))).willAnswer(invocation -> {
            QuestionId questionId = invocation.getArgument(0);
            if (Objects.equals(questionId, new QuestionId(1L))) {
                return Optional.of(question);
            } else {
                return Optional.of(question1);
            }
        });
        given(answerRepository.findAllById(any(List.class))).willAnswer(invocation -> {
            List<AnswerId> answerIds = invocation.getArgument(0);
            System.out.println(answerIds);
            if (answerIds.size() == 3) {
                return answers1;
            } else {
                return answers2;
            }
        });

        sut.submit(surveyEdition.getId(), dto);

        assertThat(answers1.getFirst().getSelectCount()).isEqualTo(1);
        assertThat(answers2.getFirst().getSelectCount()).isEqualTo(1);
        assertThat(question.getAnswerCount()).isEqualTo(1);
    }
}