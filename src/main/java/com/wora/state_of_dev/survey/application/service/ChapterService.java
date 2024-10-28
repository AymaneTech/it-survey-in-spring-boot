package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;

public interface ChapterService {
    ChapterResponseDto create(SurveyEditionId id, ChapterRequestDto dto);
}
