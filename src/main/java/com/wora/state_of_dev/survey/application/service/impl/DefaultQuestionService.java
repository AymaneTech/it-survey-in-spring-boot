package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.AnswerRequestDto;
import com.wora.state_of_dev.survey.application.dto.request.QuestionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.QuestionResponseDto;
import com.wora.state_of_dev.survey.application.mapper.AnswerMapper;
import com.wora.state_of_dev.survey.application.mapper.QuestionMapper;
import com.wora.state_of_dev.survey.application.service.QuestionService;
import com.wora.state_of_dev.survey.domain.entities.Answer;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import com.wora.state_of_dev.survey.domain.entities.Question;
import com.wora.state_of_dev.survey.domain.exception.ChapterHasSubChaptersException;
import com.wora.state_of_dev.survey.domain.repository.ChapterRepository;
import com.wora.state_of_dev.survey.domain.repository.QuestionRepository;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultQuestionService implements QuestionService {
    private final QuestionRepository repository;
    private final ChapterRepository chapterRepository;
    private final QuestionMapper mapper;
    private final AnswerMapper answerMapper;

    @Override
    public List<QuestionResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    // todo : add findByChapterId, findBySurveyEditionId

    @Override
    public QuestionResponseDto findById(QuestionId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("question", id.value()));
    }

    @Override
    public QuestionResponseDto create(QuestionRequestDto dto) {
        final ChapterId chapterId = new ChapterId(dto.chapterId());
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException("chapter", dto.chapterId()));

        if (chapterRepository.isHasSubChapters(chapterId))
            throw new ChapterHasSubChaptersException(chapterId);

        Question question = mapper.toEntity(dto)
                .setChapter(chapter)
                ._setAnswers(mapAnswersDtoToEntities(dto.answers()));

        Question savedQuestion = repository.save(question);
        return mapper.toResponseDto(savedQuestion);
    }

    @Override
    public QuestionResponseDto update(QuestionId id, QuestionRequestDto dto) {
        final ChapterId chapterId = new ChapterId(dto.chapterId());
        Question question = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("question", id.value()));

        if (chapterRepository.isHasSubChapters(chapterId))
            throw new ChapterHasSubChaptersException(chapterId);

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException("chapter", dto.chapterId()));
        question.setText(dto.text())
                .setAnswerType(dto.answerType())
                .setChapter(chapter)
                ._setAnswers(mapAnswersDtoToEntities(dto.answers()));

        return mapper.toResponseDto(question);
    }

    @Override
    public void delete(QuestionId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("question", id.value());
        repository.deleteById(id);
    }

    private List<Answer> mapAnswersDtoToEntities(List<AnswerRequestDto> answerRequestDtos) {
        return answerRequestDtos
                .stream()
                .map(answerMapper::toEntity)
                .toList();
    }
}
