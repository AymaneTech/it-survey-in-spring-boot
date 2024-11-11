package com.wora.stateOfDev.user.application.dto.response;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationResponse(@NotBlank String accessToken,
                                     @NotBlank String refreshToken
) {
}
