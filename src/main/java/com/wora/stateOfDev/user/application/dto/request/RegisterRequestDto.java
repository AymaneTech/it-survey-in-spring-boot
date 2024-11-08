package com.wora.stateOfDev.user.application.dto.request;

import com.wora.stateOfDev.common.application.validation.ReferenceExists;
import com.wora.stateOfDev.common.application.validation.UniqueField;
import com.wora.stateOfDev.user.domain.entity.Role;
import com.wora.stateOfDev.user.domain.entity.User;
import com.wora.stateOfDev.user.domain.valueObject.RoleId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDto(@NotBlank String firstName,
                                 @NotBlank String lastName,
                                 @NotBlank @Email @UniqueField(fieldName = "email", entityClass = User.class, message = "User email already exists")
                                 String email,
                                 @NotBlank String password,
                                 @NotNull @ReferenceExists(entityClass = Role.class, idClass = RoleId.class, message = "Role id does not exists")
                                 Long roleId
) {
}
