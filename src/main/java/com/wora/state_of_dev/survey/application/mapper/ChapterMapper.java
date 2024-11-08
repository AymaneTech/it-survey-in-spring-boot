package com.wora.state_of_dev.survey.application.mapper;

import com.wora.state_of_dev.common.application.mapper.IdValueObjectMapper;
import com.wora.state_of_dev.survey.application.dto.request.ChapterRequestDto;
import com.wora.state_of_dev.survey.application.dto.response.ChapterResponseDto;
import com.wora.state_of_dev.survey.domain.entity.Chapter;
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
