package com.example.demo.validator;

import com.example.demo.entity.User;
import com.example.demo.validator.impl.UserValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserValidatorImplTest {

    private UserValidatorImpl userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidatorImpl();
    }

    @Test
    @DisplayName("Проверка валидного пользователя")
    void isValid_ShouldReturnTrue_WhenUserIsValid() {
        User user = new User("john123", "password123", "john@example.com");

        boolean result = userValidator.isValid(user);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Проверка пользователя с пустым логином")
    void isValid_ShouldReturnFalse_WhenLoginIsEmpty() {
        User user = new User("", "password123", "john@example.com");
        boolean result = userValidator.isValid(user);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Проверка пользователя с коротким логином")
    void isValid_ShouldReturnFalse_WhenLoginIsTooShort() {
        User user = new User("ab", "password123", "john@example.com");
        boolean result = userValidator.isValid(user);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Проверка пользователя с коротким паролем")
    void isValid_ShouldReturnFalse_WhenPasswordIsTooShort() {
        User user = new User("john123", "pas", "john@example.com");
        boolean result = userValidator.isValid(user);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Проверка пользователя с неверным email")
    void isValid_ShouldReturnFalse_WhenEmailIsInvalid() {
        User user = new User("john123", "password123", "invalid-email");
        boolean result = userValidator.isValid(user);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Проверка, что метод validate возвращает карту ошибок")
    void validate_ShouldReturnMapWithErrors() {
        User user = new User("ab", "pas", "invalid-email");
        Map<String, String> errors = userValidator.validate(user);

        assertThat(errors).isNotEmpty();
        assertThat(errors).containsKeys("login", "password", "email");
    }
}