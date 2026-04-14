package com.example.demo.command;

import com.example.demo.command.impl.AddUserCommand;
import com.example.demo.command.impl.DefaultCommand;
import com.example.demo.command.impl.LoginCommand;
import com.example.demo.command.impl.LogoutCommand;
import com.example.demo.controller.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTypeTest {

    @Test
    @DisplayName("Должен вернуть правильную команду по имени")
    void define_ShouldReturnCorrectCommand() {
        assertThat(CommandType.define("ADD_USER")).isInstanceOf(AddUserCommand.class);
        assertThat(CommandType.define("LOGIN")).isInstanceOf(LoginCommand.class);
        assertThat(CommandType.define("LOGOUT")).isInstanceOf(LogoutCommand.class);
        assertThat(CommandType.define("UNKNOWN")).isInstanceOf(DefaultCommand.class);
        assertThat(CommandType.define(null)).isInstanceOf(DefaultCommand.class);
        assertThat(CommandType.define("")).isInstanceOf(DefaultCommand.class);
    }
}