package com.wora.stateOfDev.user.application.mapper;

import com.wora.stateOfDev.user.application.dto.request.RegisterRequestDto;
import com.wora.stateOfDev.user.application.dto.response.UserResponseDto;
import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = BaseMapper.class)
public interface AuthenticationMapper {
    @Mappings({
            @Mapping(source = "firstName", target = "name.firstName"),
            @Mapping(source = "lastName", target = "name.lastName")
    })
    User toEntity(RegisterRequestDto dto);

    @Mappings({
            @Mapping(target = "firstName", source = "name.firstName"),
            @Mapping(target = "lastName", source = "name.lastName")
    })
    UserResponseDto toResponseDto(User user);
}
