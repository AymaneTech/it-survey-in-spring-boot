package com.wora.stateOfDev.survey.application.mapper;

import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SurveyEditionMapper extends BaseMapper<SurveyEdition, SurveyEditionRequestDto, SurveyEditionResponseDto> {
}
