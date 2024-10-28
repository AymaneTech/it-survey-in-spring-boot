package com.wora.state_of_dev.common.application.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IdValueObjectMapper.class}
)
public interface BaseMapper<Entity, Request, Response> {
    Entity toEntity(Request dto);

    Response toResponseDto(Entity entity);
}
