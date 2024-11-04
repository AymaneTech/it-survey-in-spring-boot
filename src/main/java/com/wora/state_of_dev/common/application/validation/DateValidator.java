package com.wora.state_of_dev.common.application.validation;

import java.time.LocalDateTime;

public class DateValidator {

    private DateValidator() {
    }

    public static boolean isDateBetween(LocalDateTime givenDate, LocalDateTime before, LocalDateTime after) {
        if (givenDate == null || before == null || after == null) return false;
        return !givenDate.isBefore(before) && !givenDate.isAfter(after);
    }
}
