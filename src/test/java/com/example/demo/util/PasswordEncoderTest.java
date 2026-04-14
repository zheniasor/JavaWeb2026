package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {

    @Test
    @DisplayName("Хеширование пароля и проверка совпадения")
    void encodeAndMatches_ShouldWorkCorrectly() {
        String plainPassword = "mySecret123";

        String hashedPassword = PasswordEncoder.encode(plainPassword);

        assertThat(hashedPassword).isNotEqualTo(plainPassword);
        assertThat(hashedPassword).startsWith("$2a$");
        assertThat(PasswordEncoder.matches(plainPassword, hashedPassword)).isTrue();
        assertThat(PasswordEncoder.matches("wrongPassword", hashedPassword)).isFalse();
    }
}