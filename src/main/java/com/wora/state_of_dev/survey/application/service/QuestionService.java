package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.common.application.service.CrudService;
import com.wora.state_of_dev.survey.application.dto.request.QuestionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.QuestionResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.QuestionId;

public interface QuestionService extends CrudService<QuestionId, QuestionRequestDto, QuestionResponseDto> {
    // todo : add findByChapterId, findBySurveyEditionId
}
