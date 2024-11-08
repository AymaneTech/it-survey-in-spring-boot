package com.wora.stateOfDev.survey.application.service;

import com.wora.stateOfDev.common.application.service.CrudService;
import com.wora.stateOfDev.survey.application.dto.request.QuestionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.QuestionResponseDto;
import com.wora.stateOfDev.survey.domain.valueObject.QuestionId;

public interface QuestionService extends CrudService<QuestionId, QuestionRequestDto, QuestionResponseDto> {
    // todo : add findByChapterId, findBySurveyEditionId
}
