package com.wora.state_of_dev.common.application.validation;

import com.wora.state_of_dev.common.application.validation.impl.ExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistsValidator.class)
public @interface Exists {

    String message() default "This field value already exists";

    String field();

    Class<?> entity();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
