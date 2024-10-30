package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.survey.application.dto.request.QuestionRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.QuestionResponseDto;
import com.wora.state_of_dev.survey.domain.entities.Question;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface QuestionMapper extends BaseMapper<Question, QuestionRequestDto, QuestionResponseDto> {
}
