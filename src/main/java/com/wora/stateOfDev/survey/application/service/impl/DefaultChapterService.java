package com.wora.stateOfDev.survey.application.service.impl;

import com.wora.stateOfDev.common.domain.exception.EntityCreationException;
import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.survey.application.dto.request.ChapterRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.ChapterResponseDto;
import com.wora.stateOfDev.survey.application.mapper.ChapterMapper;
import com.wora.stateOfDev.survey.application.service.ChapterService;
import com.wora.stateOfDev.survey.domain.entity.Chapter;
import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
import com.wora.stateOfDev.survey.domain.repository.ChapterRepository;
import com.wora.stateOfDev.survey.domain.repository.SurveyEditionRepository;
import com.wora.stateOfDev.survey.domain.valueObject.ChapterId;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultChapterService implements ChapterService {
    private final ChapterRepository repository;
    private final SurveyEditionRepository surveyEditionRepository;
    private final ChapterMapper mapper;

    @Override
    @Cacheable(value = "chapters")
    public List<ChapterResponseDto> findAllBySurveyEditionId(SurveyEditionId id) {
        if (!surveyEditionRepository.existsById(id))
            throw new EntityNotFoundException("survey edition", id.value());

        return repository.findAllBySurveyEditionId(id)
                .stream().map(mapper::toChapterResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "chapters", key = "#id.value()")
    public ChapterResponseDto findById(ChapterId id) {
        return repository.findById(id)
                .map(mapper::toChapterResponse)
                .orElseThrow(() -> new EntityNotFoundException("chapter", id.value()));
    }

    @Override
    @CachePut(value = "chapters", key = "#result.id()")
    public ChapterResponseDto create(ChapterRequestDto dto) {
        final SurveyEditionId surveyEditionId = new SurveyEditionId(dto.surveyEditionId());
        SurveyEdition surveyEdition = surveyEditionRepository.findById(surveyEditionId)
                .orElseThrow(() -> new EntityNotFoundException("survey edition", dto.surveyEditionId()));

        if (repository.existsByTitleAndSurveyEditionId(dto.title(), surveyEditionId))
            throw new EntityCreationException("Failed to save the chapter because chapter name already used in this survey edition",
                    List.of("title (" + dto.title() + ") already exists in chapter of id " + dto.surveyEditionId()));

        Chapter chapter = mapper.toEntity(dto)
                .setSurveyEdition(surveyEdition);

        if (dto.parentChapterId() != null) {
            Chapter parentChapter = repository.findById(new ChapterId(dto.parentChapterId()))
                    .orElseThrow(() -> new EntityNotFoundException("chapter", dto.parentChapterId()));
            chapter.setParentChapter(parentChapter);
        }

        Chapter savedChapter = repository.save(chapter);
        return mapper.toChapterResponse(savedChapter);
    }

    @Override
    @CachePut(value = "chapters", key = "#id.value()")
    public ChapterResponseDto update(ChapterId id, ChapterRequestDto dto) {
        Chapter chapter = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("chapter ", id.value()));

        chapter.setTitle(dto.title());
        return mapper.toChapterResponse(chapter);
    }

    @Override
    @CacheEvict(value = "chapters", allEntries = true)
    public void delete(ChapterId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("chapter", id.value());
        repository.deleteById(id);
    }
}
