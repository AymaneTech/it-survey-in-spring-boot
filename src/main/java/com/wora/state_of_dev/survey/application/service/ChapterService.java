package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;
import com.wora.state_of_dev.survey.domain.valueObject.SurveyEditionId;

import java.util.List;

public interface ChapterService {
    List<ChapterResponseDto> findAllBySurveyEditionId(SurveyEditionId id);

    ChapterResponseDto findById(ChapterId id);

    ChapterResponseDto create(SurveyEditionId id, ChapterRequestDto dto);

    ChapterResponseDto update(ChapterId id, ChapterRequestDto dto);

    void delete(ChapterId id);
}
