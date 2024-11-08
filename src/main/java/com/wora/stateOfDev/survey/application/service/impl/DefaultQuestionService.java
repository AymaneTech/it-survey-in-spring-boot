package com.wora.stateOfDev.survey.application.service.impl;

import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.survey.application.dto.request.AnswerRequestDto;
import com.wora.stateOfDev.survey.application.dto.request.QuestionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.QuestionResponseDto;
import com.wora.stateOfDev.survey.application.mapper.AnswerMapper;
import com.wora.stateOfDev.survey.application.mapper.QuestionMapper;
import com.wora.stateOfDev.survey.application.service.QuestionService;
import com.wora.stateOfDev.survey.domain.entity.Answer;
import com.wora.stateOfDev.survey.domain.entity.Chapter;
import com.wora.stateOfDev.survey.domain.entity.Question;
import com.wora.stateOfDev.survey.domain.exception.AnswersCannotBeEmptyException;
import com.wora.stateOfDev.survey.domain.exception.ChapterHasSubChaptersException;
import com.wora.stateOfDev.survey.domain.repository.ChapterRepository;
import com.wora.stateOfDev.survey.domain.repository.QuestionRepository;
import com.wora.stateOfDev.survey.domain.valueObject.ChapterId;
import com.wora.stateOfDev.survey.domain.valueObject.QuestionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.wora.stateOfDev.common.util.OptionalWrapper.orElseThrow;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultQuestionService implements QuestionService {
    private final QuestionRepository repository;
    private final ChapterRepository chapterRepository;
    private final QuestionMapper mapper;
    private final AnswerMapper answerMapper;

    // todo : add findByChapterId, findBySurveyEditionId

    @Override
    @Cacheable(value = "questions")
    public List<QuestionResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Cacheable(value = "questions", key = "#id.value()")
    public QuestionResponseDto findById(QuestionId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("question", id.value()));
    }

    @Override
    @CachePut(value = "questions", key = "#result.id()")
    public QuestionResponseDto create(QuestionRequestDto dto) {
        final ChapterId chapterId = new ChapterId(dto.chapterId());
        Chapter chapter = orElseThrow(chapterRepository.findById(chapterId), "chapter", dto.chapterId());

        ensureHasNoSubChapters(chapterId);
        Question question = mapper.toEntity(dto)
                .setChapter(chapter)
                ._setAnswers(mapAnswersDtoToEntities(dto.answers()));

        Question savedQuestion = repository.save(question);
        return mapper.toResponseDto(savedQuestion);
    }

    @Override
    @CachePut(value = "questions", key = "#id.value()")
    public QuestionResponseDto update(QuestionId id, QuestionRequestDto dto) {
        final ChapterId chapterId = new ChapterId(dto.chapterId());
        Question question = orElseThrow(repository.findById(id), "question", id.value());

        ensureHasNoSubChapters(chapterId);
        Chapter chapter = orElseThrow(chapterRepository.findById(chapterId), "chapter", dto.chapterId());

        question.setText(dto.text())
                .setAnswerType(dto.answerType())
                .setChapter(chapter)
                ._setAnswers(mapAnswersDtoToEntities(dto.answers()));

        return mapper.toResponseDto(question);
    }

    @Override
    @CacheEvict(value = "questions", allEntries = true)
    public void delete(QuestionId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("question", id.value());
        repository.deleteById(id);
    }

    private List<Answer> mapAnswersDtoToEntities(List<AnswerRequestDto> answerRequestDtos) {
        if (answerRequestDtos.isEmpty())
            throw new AnswersCannotBeEmptyException("Cannot create question without answers");

        return answerRequestDtos
                .stream()
                .map(answerMapper::toEntity)
                .toList();
    }

    private void ensureHasNoSubChapters(ChapterId chapterId) {
        if (chapterRepository.isHasSubChapters(chapterId))
            throw new ChapterHasSubChaptersException(chapterId);
    }
}
