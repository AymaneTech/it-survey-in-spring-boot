package com.wora.stateOfDev.survey.application.mapper;

import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.survey.application.dto.request.SurveyRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyResponseDto;
import com.wora.stateOfDev.survey.domain.entity.Survey;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SurveyMapper extends BaseMapper<Survey, SurveyRequestDto, SurveyResponseDto> {
}
