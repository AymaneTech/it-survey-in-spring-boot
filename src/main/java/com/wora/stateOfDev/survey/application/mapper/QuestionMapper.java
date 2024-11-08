package com.wora.stateOfDev.survey.application.mapper;

import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.survey.application.dto.request.QuestionRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.QuestionResponseDto;
import com.wora.stateOfDev.survey.domain.entity.Question;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface QuestionMapper extends BaseMapper<Question, QuestionRequestDto, QuestionResponseDto> {
}
