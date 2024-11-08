package com.wora.stateOfDev.common.domain.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Object id;

    public EntityNotFoundException(String entityName, Object id) {
        super(entityName + " with id " + id + " not found");
        this.id = id;
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.id = null;
    }
}
