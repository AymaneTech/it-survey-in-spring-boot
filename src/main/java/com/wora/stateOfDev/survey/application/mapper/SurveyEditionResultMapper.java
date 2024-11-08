package com.wora.stateOfDev.survey.application.mapper;

import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.survey.application.dto.response.SurveyEditionResultResponseDto;
import com.wora.stateOfDev.survey.domain.entity.SurveyEdition;
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
