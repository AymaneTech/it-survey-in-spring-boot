package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResultResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;

public interface SurveyEditionResultService {

    SurveyEditionResultResponseDto getResult(SurveyEditionId id);
}
