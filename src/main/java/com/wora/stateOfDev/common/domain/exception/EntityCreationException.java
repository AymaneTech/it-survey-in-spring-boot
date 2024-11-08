package com.wora.stateOfDev.common.domain.exception;

import java.util.List;

public class EntityCreationException extends RuntimeException {
    private final List<String> errors;

    public EntityCreationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> errors() {
        return errors;
    }
}
