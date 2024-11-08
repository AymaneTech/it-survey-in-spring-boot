package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.application.validation.validator.DateValidator;
import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.submission.*;
import com.wora.state_of_dev.survey.application.service.SurveySubmissionService;
import com.wora.state_of_dev.survey.domain.entity.Answer;
import com.wora.state_of_dev.survey.domain.entity.Question;
import com.wora.state_of_dev.survey.domain.entity.SurveyEdition;
import com.wora.state_of_dev.survey.domain.exception.GivenAnswerNotBelongToQuestion;
import com.wora.state_of_dev.survey.domain.exception.QuestionNotBelongToSurveyEdition;
import com.wora.state_of_dev.survey.domain.exception.SurveyEditionNotOpenedNow;
import com.wora.state_of_dev.survey.domain.repository.AnswerRepository;
import com.wora.state_of_dev.survey.domain.repository.QuestionRepository;
import com.wora.state_of_dev.survey.domain.repository.SurveyEditionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerId;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.data.RepositoryMetricsAutoConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultSurveySubmissionService implements SurveySubmissionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyEditionRepository surveyEditionRepository;
    private final RepositoryMetricsAutoConfiguration repositoryMetricsAutoConfiguration;

    private SurveyEditionId surveyEditionId;

    @Override
    public void submit(SurveyEditionId surveyEditionId, SingleQuestionSubmissionRequestDto dto) {
        this.surveyEditionId = surveyEditionId;
        handleSubmission(dto);
    }

    @Override
    public void submit(SurveyEditionId surveyEditionId, ListOfQuestionSubmissionRequestDto dto) {
        this.surveyEditionId = surveyEditionId;
        dto.submissions()
                .forEach(this::handleSubmission);
    }

    private void handleSubmission(SingleQuestionSubmissionRequestDto dto) {
        validateParticipationDateIsBetweenSurveyEditionStartAndEndDate();

        Question question = findQuestionById(dto.questionId());
        if (!Objects.equals(surveyEditionId.value(), question.getChapter().getSurveyEdition().getId().value()))
            throw new QuestionNotBelongToSurveyEdition("the question with ID: " + question.getId().value() + " does not belong to survey of id " + surveyEditionId.value());

        processAnswer(dto.answer(), question);
        question.incrementAnswerCount();
    }

    private void validateParticipationDateIsBetweenSurveyEditionStartAndEndDate() {
        SurveyEdition surveyEdition = surveyEditionRepository.findById(surveyEditionId)
                .orElseThrow(() -> new EntityNotFoundException("survey edition", surveyEditionId.value()));
        if (!DateValidator.isDateBetween(LocalDateTime.now(), surveyEdition.getStartDate(), surveyEdition.getEndDate())) {
            throw new SurveyEditionNotOpenedNow("you are trying to participate in a closed survey edition");
        }
    }

    private Question findQuestionById(Long questionId) {
        return questionRepository.findById(new QuestionId(questionId))
                .orElseThrow(() -> new EntityNotFoundException("question", questionId));
    }

    private void processAnswer(AnswerSubmissionRequestDto<?> answerDto, Question question) {
        if (answerDto instanceof SingleChoiceSubmissionRequestDto singleChoiceAnswer
                && question.getAnswerType().equals(AnswerType.SINGLE_CHOICE)) {
            processSingleChoiceAnswer(singleChoiceAnswer, question);
        } else if (answerDto instanceof MultiChoiceSubmissionRequestDto multiChoiceAnswer
                && question.getAnswerType().equals(AnswerType.MULTI_CHOICE)) {
            processMultiChoiceAnswer(multiChoiceAnswer, question);
        } else {
            throw new IllegalArgumentException("Unsupported answer type");
        }
    }

    private void processSingleChoiceAnswer(SingleChoiceSubmissionRequestDto answerDto, Question question) {
        Long answerId = answerDto.answer();
        Answer answer = answerRepository.findById(new AnswerId(answerId))
                .orElseThrow(() -> new EntityNotFoundException("answer", answerId));
        incrementSelectionCount(question, answer);
    }

    private void processMultiChoiceAnswer(MultiChoiceSubmissionRequestDto answerDto, Question question) {
        List<AnswerId> answerIds = answerDto.answer().stream()
                .map(AnswerId::new)
                .toList();
        List<Answer> answers = answerRepository.findAllById(answerIds);

        if (answers.size() != answerIds.size())
            throw new EntityNotFoundException("Some of the answer ids not found: " + answerIds);

        answers.forEach(answer -> incrementSelectionCount(question, answer));
    }

    private void incrementSelectionCount(Question question, Answer answer) {
        if (answer.getQuestion().getId() != question.getId())
            throw new GivenAnswerNotBelongToQuestion("answer with id " + answer.getId().value() + " does not related to question " + question.getId().value());
        answer.incrementSelectCount();
    }
}