package com.wora.state_of_dev.survey.application.service.impl;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.owner.domain.Owner;
import com.wora.state_of_dev.owner.domain.OwnerId;
import com.wora.state_of_dev.owner.domain.OwnerRepository;
import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.application.mapper.SurveyMapper;
import com.wora.state_of_dev.survey.application.service.SurveyService;
import com.wora.state_of_dev.survey.domain.entities.Survey;
import com.wora.state_of_dev.survey.domain.repository.SurveyRepository;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;
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
public class DefaultSurveyService implements SurveyService {
    private final SurveyRepository repository;
    private final OwnerRepository ownerRepository;
    private final SurveyMapper mapper;

    @Override
    @Cacheable(value = "surveys")
    public List<SurveyResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Cacheable(value = "surveys", key = "#id.value()")
    public SurveyResponseDto findById(SurveyId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("survey", id.value()));

    }

    @Override
    @CachePut(value = "surveys", key = "#result.id()")
    public SurveyResponseDto create(SurveyRequestDto dto) {
        Owner owner = ownerRepository.findById(new OwnerId(dto.ownerId()))
                .orElseThrow(() -> new EntityNotFoundException("owner", dto.ownerId()));
        Survey survey = mapper.toEntity(dto)
                .setOwner(owner);

        Survey savedSurvey = repository.save(survey);
        return mapper.toResponseDto(savedSurvey);
    }

    @Override
    @CachePut(value = "surveys", key = "#result.id()")
    public SurveyResponseDto update(SurveyId id, SurveyRequestDto dto) {
        Survey survey = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("survey", id.value()));
        Owner owner = ownerRepository.findById(new OwnerId(dto.ownerId()))
                .orElseThrow(() -> new EntityNotFoundException("owner", dto.ownerId()));

        survey.setTitle(dto.title())
                .setDescription(dto.description())
                .setOwner(owner);

        return mapper.toResponseDto(survey);
    }

    @Override
    @CacheEvict(value = "surveys", allEntries = true)
    public void delete(SurveyId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("survey", id.value());

        repository.deleteById(id);
    }
}
