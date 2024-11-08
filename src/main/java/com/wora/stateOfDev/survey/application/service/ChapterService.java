package com.wora.stateOfDev.survey.application.service;

import com.wora.stateOfDev.survey.application.dto.request.ChapterRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.ChapterResponseDto;
import com.wora.stateOfDev.survey.domain.valueObject.ChapterId;
import com.wora.stateOfDev.survey.domain.valueObject.SurveyEditionId;

import java.util.List;

public interface ChapterService {
    List<ChapterResponseDto> findAllBySurveyEditionId(SurveyEditionId id);

    ChapterResponseDto findById(ChapterId id);

    ChapterResponseDto create(ChapterRequestDto dto);

    ChapterResponseDto update(ChapterId id, ChapterRequestDto dto);

    void delete(ChapterId id);
}
