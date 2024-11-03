package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.*;
import com.wora.state_of_dev.survey.application.service.SurveySubmissionService;
import com.wora.state_of_dev.survey.domain.entities.Answer;
import com.wora.state_of_dev.survey.domain.entities.Question;
import com.wora.state_of_dev.survey.domain.exception.GivenAnswerNotBelongToQuestion;
import com.wora.state_of_dev.survey.domain.exception.QuestionNotBelongToSurveyEdition;
import com.wora.state_of_dev.survey.domain.repository.AnswerRepository;
import com.wora.state_of_dev.survey.domain.repository.QuestionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerId;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerType;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultSurveySubmissionService implements SurveySubmissionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

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
        Question question = findQuestionById(dto.questionId());
        System.out.println("---------------------------------------");
        System.out.println("survey edition " + surveyEditionId);
        System.out.println("survey edition of question " + question.getChapter().getSurveyEdition().getId());
        if (!Objects.equals(surveyEditionId.value(), question.getChapter().getSurveyEdition().getId().value()))
            throw new QuestionNotBelongToSurveyEdition("the question with ID: " + question.getId().value() + " does not belong to survey of id " + surveyEditionId.value());

        question.incrementAnswerCount();
        processAnswer(dto.answer(), question);
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
        answerRepository.findAllById(answerIds)
                .forEach(answer -> incrementSelectionCount(question, answer));
    }

    private void incrementSelectionCount(Question question, Answer answer) {
        if (answer.getQuestion().getId() != question.getId())
            throw new GivenAnswerNotBelongToQuestion("answer with id " + answer.getId().value() + " does not related to question " + question.getId().value());
        answer.incrementSelectCount();
    }
}