package com.wora.stateOfDev.survey.application.mapper;

import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.survey.application.dto.request.AnswerRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.AnswerResponseDto;
import com.wora.stateOfDev.survey.domain.entity.Answer;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface AnswerMapper extends BaseMapper<Answer, AnswerRequestDto, AnswerResponseDto> {
}
