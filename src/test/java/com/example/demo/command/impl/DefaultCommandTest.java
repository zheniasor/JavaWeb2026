package com.example.demo.command.impl;

import com.example.demo.command.PageConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultCommandTest {

    @Test
    @DisplayName("Должен вернуть index.jsp")
    void execute_ShouldReturnIndexPage() {
        DefaultCommand command = new DefaultCommand();
        String result = command.execute(null);
        assertThat(result).isEqualTo(PageConstants.INDEX_PAGE);
    }
}