package com.wora.stateOfDev.survey.application.mapper;

import com.wora.stateOfDev.common.application.mapper.IdValueObjectMapper;
import com.wora.stateOfDev.survey.application.dto.request.ChapterRequestDto;
import com.wora.stateOfDev.survey.application.dto.response.ChapterResponseDto;
import com.wora.stateOfDev.survey.domain.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IdValueObjectMapper.class}
)
public interface ChapterMapper {
    Chapter toEntity(ChapterRequestDto dto);

    ChapterResponseDto toChapterResponse(Chapter chapter);
}
