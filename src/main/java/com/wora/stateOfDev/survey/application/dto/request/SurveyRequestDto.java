package com.wora.stateOfDev.survey.application.dto.request;

import com.wora.stateOfDev.common.application.validation.ReferenceExists;
import com.wora.stateOfDev.common.application.validation.UniqueField;
import com.wora.stateOfDev.owner.domain.Owner;
import com.wora.stateOfDev.owner.domain.OwnerId;
import com.wora.stateOfDev.survey.domain.entity.Survey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyRequestDto(
        @NotBlank @UniqueField(fieldName = "title", entityClass = Survey.class, message = "survey title already exists")
        String title,
        @NotBlank String description,
        @NotNull @ReferenceExists(idClass = OwnerId.class, entityClass = Owner.class, message = "owner id does not exists ")
        Long ownerId
) {
}
