package com.wora.stateOfDev.common.domain;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(int code, LocalDateTime timestamp, String message, String description, Object errors) {
}

