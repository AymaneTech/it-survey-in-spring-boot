package com.wora.state_of_dev.common.application.mapper;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface IdValueObjectMapper {

    default Long valueObjectToUuid(Object vo) {
        if (vo == null) {
            return null;
        }
        try {
            return (Long) vo.getClass().getMethod("value").invoke(vo);
        } catch (Exception e) {
            return null;
        }
    }
}
