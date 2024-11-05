package com.wora.state_of_dev.survey.application.dto.request;

import com.wora.state_of_dev.common.application.validation.ReferenceExists;
import com.wora.state_of_dev.common.application.validation.UniqueField;
import com.wora.state_of_dev.owner.domain.Owner;
import com.wora.state_of_dev.owner.domain.OwnerId;
import com.wora.state_of_dev.survey.domain.entities.Survey;
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
