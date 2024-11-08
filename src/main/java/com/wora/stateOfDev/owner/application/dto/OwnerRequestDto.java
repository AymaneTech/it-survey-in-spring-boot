package com.wora.stateOfDev.owner.application.dto;

import com.wora.stateOfDev.common.application.validation.UniqueField;
import com.wora.stateOfDev.owner.domain.Owner;
import jakarta.validation.constraints.NotBlank;

public record OwnerRequestDto(
        @NotBlank
        @UniqueField(fieldName = "name", entityClass = Owner.class, message = "Owner name already exists")
        String name) {
}
