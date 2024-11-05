// ShouldExistsValidator.java
package com.wora.state_of_dev.common.application.validation.validator;

import com.wora.state_of_dev.common.application.validation.ReferenceExists;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@RequiredArgsConstructor
public class ReferenceExistsValidator implements ConstraintValidator<ReferenceExists, Long> {

    private final static String FIELD_NAME = "id";

    private final EntityManager entityManager;
    private Class<?> entityClass;
    private Class<?> idClass;

    @Override
    public void initialize(ReferenceExists constraintAnnotation) {
        entityClass = constraintAnnotation.entityClass();
        idClass = constraintAnnotation.idClass();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        String queryStr = String.format(
                "SELECT COUNT(e) FROM %s e WHERE e.id = :value",
                entityClass.getSimpleName()
        );

        Long count = entityManager.createQuery(queryStr, Long.class)
                .setParameter("value", getId(value))
                .getSingleResult();
        return count > 0;
    }

    @SuppressWarnings("unchecked")
    private <T> T getId(Long id) {
        try {
            return (T) idClass.getConstructor(Long.class).newInstance(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}