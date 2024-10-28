package com.wora.state_of_dev.owner.application.service;

import com.wora.state_of_dev.common.application.service.CrudService;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.domain.OwnerId;

public interface OwnerService extends CrudService<OwnerId, OwnerRequestDto, OwnerResponseDto> {
}
