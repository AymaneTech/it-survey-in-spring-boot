package com.wora.state_of_dev.common.application.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateValidatorTest {

    @Test
    void isDateBetween_ShouldReturnFalse_WhenGivenPastDate() {
        LocalDateTime givenDate = LocalDateTime.now().minusDays(21);
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusDays(20);

        boolean actual = DateValidator.isDateBetween(givenDate, before, after);

        assertFalse(actual);
    }

    @Test
    void isDateBetween_ShouldReturnFalse_WhenGivenNotValidDate() {
        LocalDateTime givenDate = LocalDateTime.now().plusDays(30);
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusDays(20);

        boolean actual = DateValidator.isDateBetween(givenDate, before, after);

        assertFalse(actual);
    }

    @Test
    void isDateBetween_shouldReturnFalse_whenGivenNullValues() {
        boolean actual = DateValidator.isDateBetween(null, null, null);

        assertFalse(actual);
    }

    @Test
    void isDateBetween_ShouldReturnTrue_WhenGivenNotValidDate() {
        LocalDateTime givenDate = LocalDateTime.now().plusDays(10);
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusDays(20);

        boolean actual = DateValidator.isDateBetween(givenDate, before, after);

        assertTrue(actual);
    }
}