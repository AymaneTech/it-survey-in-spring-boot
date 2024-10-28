package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.application.service.CrudService;
import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyId;

public interface SurveyService extends CrudService<SurveyId, SurveyRequestDto, SurveyResponseDto> {
}
