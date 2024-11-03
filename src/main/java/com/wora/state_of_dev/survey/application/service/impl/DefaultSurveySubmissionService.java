package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.ListOfQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.MultiChoiceSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SingleChoiceSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.SingleQuestionSubmissionRequestDto;
import com.wora.state_of_dev.survey.application.service.SurveySubmissionService;
import com.wora.state_of_dev.survey.domain.entities.Answer;
import com.wora.state_of_dev.survey.domain.entities.Question;
import com.wora.state_of_dev.survey.domain.repository.AnswerRepository;
import com.wora.state_of_dev.survey.domain.repository.QuestionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.AnswerId;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultSurveySubmissionService implements SurveySubmissionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public void submit(SingleQuestionSubmissionRequestDto dto) {
        Question question = findQuestionById(dto.questionId());
        question.incrementAnswerCount();
        processAnswer(dto.answer());
    }

    private Question findQuestionById(Long questionId) {
        return questionRepository.findById(new QuestionId(questionId))
                .orElseThrow(() -> new EntityNotFoundException("question", questionId));
    }

    private void processAnswer(Object answerDto) {
        if (answerDto instanceof SingleChoiceSubmissionRequestDto singleChoiceAnswer) {
            processSingleChoiceAnswer(singleChoiceAnswer);
        } else if (answerDto instanceof MultiChoiceSubmissionRequestDto multiChoiceAnswer) {
            processMultiChoiceAnswer(multiChoiceAnswer);
        } else {
            throw new IllegalArgumentException("Unsupported answer type");
        }
    }

    private void processSingleChoiceAnswer(SingleChoiceSubmissionRequestDto answerDto) {
        Long answerId = answerDto.answer();
        Answer answer = answerRepository.findById(new AnswerId(answerId))
                .orElseThrow(() -> new EntityNotFoundException("answer", answerId));
        answer.incrementSelectCount();
    }

    private void processMultiChoiceAnswer(MultiChoiceSubmissionRequestDto answerDto) {
        List<AnswerId> answerIds = answerDto.answer().stream()
                .map(AnswerId::new)
                .toList();
        List<Answer> answers = answerRepository.findAllById(answerIds);
        answers.forEach(Answer::incrementSelectCount);
    }

    @Override
    public void submit(ListOfQuestionSubmissionRequestDto dto) {

    }
}