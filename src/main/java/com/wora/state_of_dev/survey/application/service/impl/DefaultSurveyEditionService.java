package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.state_of_dev.survey.application.mapper.SurveyEditionMapper;
import com.wora.state_of_dev.survey.application.service.SurveyEditionService;
import com.wora.state_of_dev.survey.domain.entities.Survey;
import com.wora.state_of_dev.survey.domain.entities.SurveyEdition;
import com.wora.state_of_dev.survey.domain.repository.SurveyEditionRepository;
import com.wora.state_of_dev.survey.domain.repository.SurveyRepository;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public List<SurveyEditionResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public SurveyEditionResponseDto findById(SurveyEditionId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("survey edition  ", id));
    }

    @Override
    public SurveyEditionResponseDto create(SurveyEditionRequestDto dto) {
        Survey survey = surveyRepository.findById(new SurveyId(dto.surveyId()))
                .orElseThrow(() -> new EntityNotFoundException("survey", dto.surveyId()));

        SurveyEdition surveyEdition = mapper.toEntity(dto)
                .setSurvey(survey);
        SurveyEdition savedEdition = repository.save(surveyEdition);
        return mapper.toResponseDto(savedEdition);
    }

    @Override
    public SurveyEditionResponseDto update(SurveyEditionId id, SurveyEditionRequestDto dto) {
        SurveyEdition surveyEdition = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("survey edition", id));
        Survey survey = surveyRepository.findById(new SurveyId(dto.surveyId()))
                .orElseThrow(() -> new EntityNotFoundException("survey", dto.surveyId()));

        surveyEdition.setStartDate(dto.startDate())
                .setEndDate(dto.endDate())
                .setYear(dto.year())
                .setSurvey(survey);

        return mapper.toResponseDto(surveyEdition);
    }

    @Override
    public void delete(SurveyEditionId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("survey edition", id);
        repository.deleteById(id);
    }
}
