package com.wora.state_of_dev.owner.application.dto;

import com.wora.state_of_dev.common.application.validation.UniqueField;
import com.wora.state_of_dev.owner.domain.Owner;
import jakarta.validation.constraints.NotBlank;

public record OwnerRequestDto(
        @NotBlank
        @UniqueField(fieldName = "name", entityClass = Owner.class, message = "Owner name already exists")
        String name) {
}
