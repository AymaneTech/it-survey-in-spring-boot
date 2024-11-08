package com.wora.stateOfDev.survey.application.service.impl;

import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResultResponseDto;
import com.wora.stateOfDev.survey.application.mapper.SurveyEditionResultMapper;
import com.wora.stateOfDev.survey.application.service.SurveyEditionResultService;
import com.wora.stateOfDev.survey.domain.entity.Chapter;
import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
import com.wora.stateOfDev.survey.domain.repository.SurveyEditionRepository;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultSurveyEditionResultService implements SurveyEditionResultService {
    private final SurveyEditionRepository repository;
    private final SurveyEditionResultMapper mapper;

    @Override
    public SurveyEditionResultResponseDto getResult(SurveyEditionId id) {
        SurveyEdition surveyEdition = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("survey edition", id.value()));

        List<Chapter> parentChapters = surveyEdition.getChapters()
                .stream().filter(Chapter::isSurveyEditionParent)
                .toList();
        surveyEdition.setChapters(parentChapters);

        return mapper.toResponse(surveyEdition);
    }
}
