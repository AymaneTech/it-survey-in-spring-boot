package com.wora.stateOfDev.survey.application.service;

import com.wora.stateOfDev.common.application.service.CrudService;
import com.wora.stateOfDev.survey.application.dto.request.SurveyRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyResponseDto;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyId;

public interface SurveyService extends CrudService<SurveyId, SurveyRequestDto, SurveyResponseDto> {
}
