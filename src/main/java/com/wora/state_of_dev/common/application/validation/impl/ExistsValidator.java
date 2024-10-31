package com.wora.state_of_dev.common.application.validation.impl;

import com.wora.state_of_dev.common.application.validation.Exists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ExistsValidator implements ConstraintValidator<Exists, String> {
    private final ApplicationContext applicationContext;

    private Class<?> entity;
    private String field;

    @Override
    public void initialize(Exists constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            String repositoryBeanName = entity.getSimpleName() + "Repository";
            JpaRepository<?, ?> repository = (JpaRepository<?, ?>) applicationContext.getBean(repositoryBeanName);
            System.out.println("here is the repo");
            System.out.println(repository);

            String methodName = "existsBy" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
            Method method = repository.getClass().getMethod(methodName, String.class);
            Boolean exists = (Boolean) method.invoke(repository, value);

            return !exists;
        } catch (Exception e) {
            throw new RuntimeException("Error invoking repository method", e);
        }
    }
}
