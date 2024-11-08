package com.wora.stateOfDev.survey.application.service;

import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResultResponseDto;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;

public interface SurveyEditionResultService {

    SurveyEditionResultResponseDto getResult(SurveyEditionId id);
}
