package com.wora.stateOfDev.survey.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequestDto(@NotBlank String text){
}
