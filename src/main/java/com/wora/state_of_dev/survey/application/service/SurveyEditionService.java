package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.application.service.CrudService;
import com.wora.state_of_dev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;

public interface SurveyEditionService extends CrudService<SurveyEditionId, SurveyEditionRequestDto, SurveyEditionResponseDto> {
}
