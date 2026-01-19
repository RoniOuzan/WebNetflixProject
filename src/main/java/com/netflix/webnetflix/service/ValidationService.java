package com.netflix.webnetflix.service;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final Validator validator;

    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T obj) {
        var violations = validator.validate(obj);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> v : violations) {
                sb.append(v.getPropertyPath())
                        .append(": ")
                        .append(v.getMessage())
                        .append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
}

