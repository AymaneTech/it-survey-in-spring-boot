package com.wora.stateOfDev.common.application.validation.validator;

import java.time.LocalDateTime;

public class DateValidator {

    private DateValidator() {
    }

    public static boolean isDateBetween(LocalDateTime givenDate, LocalDateTime before, LocalDateTime after) {
        if (givenDate == null || before == null || after == null) return false;
        return !givenDate.isBefore(before) && !givenDate.isAfter(after);
    }
}
