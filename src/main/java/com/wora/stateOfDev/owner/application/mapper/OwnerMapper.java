package com.wora.stateOfDev.owner.application.mapper;

import com.wora.stateOfDev.common.application.mapper.BaseMapper;
import com.wora.stateOfDev.owner.application.dto.OwnerRequestDto;
import com.wora.stateOfDev.owner.application.dto.OwnerResponseDto;
import com.wora.stateOfDev.owner.domain.Owner;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface OwnerMapper extends BaseMapper<Owner, OwnerRequestDto, OwnerResponseDto> {
}
