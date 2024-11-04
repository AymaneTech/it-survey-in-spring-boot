package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.survey.application.dto.response.SurveyEditionResultResponseDto;
import com.wora.state_of_dev.survey.domain.entities.SurveyEdition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = BaseMapper.class)
public interface SurveyEditionResultMapper {
    @Mappings({
            @Mapping(source = "survey.title", target = "title"),
            @Mapping(source = "survey.description", target = "description")
    })
    SurveyEditionResultResponseDto toResponse(SurveyEdition surveyEdition);
}
