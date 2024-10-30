package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.survey.application.dto.request.AnswerRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.AnswerResponseDto;
import com.wora.state_of_dev.survey.domain.entities.Answer;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface AnswerMapper extends BaseMapper<Answer, AnswerRequestDto, AnswerResponseDto> {
}
