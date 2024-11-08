package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.survey.application.dto.request.SurveyRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyResponseDto;
import com.wora.state_of_dev.survey.domain.entity.Survey;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SurveyMapper extends BaseMapper<Survey, SurveyRequestDto, SurveyResponseDto> {
}
