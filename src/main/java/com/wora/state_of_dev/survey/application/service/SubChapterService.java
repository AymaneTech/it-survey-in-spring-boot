package com.wora.state_of_dev.survey.application.service;

import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SubChapterResponseDto;
import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;

public interface SubChapterService {
    SubChapterResponseDto create(ChapterId parentId, ChapterRequestDto dto);
}
