package com.example.demo.validator.impl;

import com.example.demo.entity.User;
import com.example.demo.validator.UserValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class UserValidatorImpl implements UserValidator {
    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize Jakarta Validator: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> validate(User user) {
        return VALIDATOR.validate(user).stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public boolean isValid(User user) {
        return VALIDATOR.validate(user).isEmpty();
    }
}