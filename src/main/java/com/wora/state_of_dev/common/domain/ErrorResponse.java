package com.wora.state_of_dev.common.domain;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(int code, LocalDateTime timestamp, String message, String description, Map<String, String> errors) {
}

