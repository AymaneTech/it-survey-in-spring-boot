package com.wora.stateOfDev.common.util;

import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;

import java.util.Optional;

public class OptionalWrapper {
    public static <T> T orElseThrow(Optional<T> optional, String entity, Object id) {
        return optional.orElseThrow(() -> new EntityNotFoundException(entity, id));
    }

    public static <T> T orElseThrow(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new EntityNotFoundException(message));
    }


}
