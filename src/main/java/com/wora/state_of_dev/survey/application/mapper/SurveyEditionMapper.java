package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.survey.application.dto.request.SurveyEditionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResponseDto;
import com.wora.state_of_dev.survey.domain.entity.SurveyEdition;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SurveyEditionMapper extends BaseMapper<SurveyEdition, SurveyEditionRequestDto, SurveyEditionResponseDto> {
}
