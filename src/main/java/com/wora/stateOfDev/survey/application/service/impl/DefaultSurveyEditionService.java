package com.wora.stateOfDev.survey.application.service.impl;

import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.stateOfDev.survey.application.mapper.SurveyEditionMapper;
import com.wora.stateOfDev.survey.application.service.SurveyEditionService;
import com.wora.stateOfDev.survey.domain.entity.Survey;
import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
import com.wora.stateOfDev.survey.domain.repository.SurveyEditionRepository;
import com.wora.stateOfDev.survey.domain.repository.SurveyRepository;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyId;
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
public class DefaultSurveyEditionService implements SurveyEditionService {
    private final SurveyEditionRepository repository;
    private final SurveyRepository surveyRepository;
    private final SurveyEditionMapper mapper;

    @Override
    @Cacheable(value = "survey_editions")
    public List<SurveyEditionResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Cacheable(value = "survey_editions", key = "#id.value()")
    public SurveyEditionResponseDto findById(SurveyEditionId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("survey edition  ", id.value()));
    }

    @Override
    @CachePut(value = "survey_editions", key = "#result.id()")
    public SurveyEditionResponseDto create(SurveyEditionRequestDto dto) {
        Survey survey = surveyRepository.findById(new SurveyId(dto.surveyId()))
                .orElseThrow(() -> new EntityNotFoundException("survey", dto.surveyId()));

        SurveyEdition surveyEdition = mapper.toEntity(dto)
                .setSurvey(survey);
        SurveyEdition savedEdition = repository.save(surveyEdition);
        return mapper.toResponseDto(savedEdition);
    }

    @Override
    @CachePut(value = "survey_editions", key = "#id.value()")
    public SurveyEditionResponseDto update(SurveyEditionId id, SurveyEditionRequestDto dto) {
        SurveyEdition surveyEdition = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("survey edition", id.value()));
        Survey survey = surveyRepository.findById(new SurveyId(dto.surveyId()))
                .orElseThrow(() -> new EntityNotFoundException("survey", dto.surveyId()));

        surveyEdition.setStartDate(dto.startDate())
                .setEndDate(dto.endDate())
                .setYear(dto.year())
                .setSurvey(survey);

        return mapper.toResponseDto(surveyEdition);
    }

    @Override
    @CacheEvict(value = "survey_editions", allEntries = true)
    public void delete(SurveyEditionId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("survey edition", id.value());
        repository.deleteById(id);
    }
}
