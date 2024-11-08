package com.wora.stateOfDev.owner.application.service;

import com.wora.stateOfDev.common.application.service.CrudService;
import com.wora.stateOfDev.owner.application.dto.OwnerRequestDto;
import com.wora.stateOfDev.owner.application.dto.OwnerResponseDto;
import com.wora.stateOfDev.owner.domain.OwnerId;

public interface OwnerService extends CrudService<OwnerId, OwnerRequestDto, OwnerResponseDto> {
}
