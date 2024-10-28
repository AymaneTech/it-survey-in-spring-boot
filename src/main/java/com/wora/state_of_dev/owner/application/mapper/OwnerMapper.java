package com.wora.state_of_dev.owner.application.mapper;

import com.wora.state_of_dev.common.application.mapper.BaseMapper;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.domain.Owner;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface OwnerMapper extends BaseMapper<Owner, OwnerRequestDto, OwnerResponseDto> {
}
