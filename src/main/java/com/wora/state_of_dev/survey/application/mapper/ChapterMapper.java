package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.domain.entities.Chapter;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface ChapterMapper extends BaseMapper<Chapter, ChapterRequestDto, ChapterResponseDto> {
}
