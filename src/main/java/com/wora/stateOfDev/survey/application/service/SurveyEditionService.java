package com.wora.stateOfDev.survey.application.service;

import com.wora.stateOfDev.common.application.service.CrudService;
import com.wora.stateOfDev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;

public interface SurveyEditionService extends CrudService<SurveyEditionId, SurveyEditionRequestDto, SurveyEditionResponseDto> {
}
