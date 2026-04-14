package com.example.demo.command;

import com.example.demo.command.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {
    ADD_USER("ADD_USER", new AddUserCommand()),
    LOGIN("LOGIN", new LoginCommand()),
    LOGOUT("LOGOUT", new LogoutCommand()),
    CONFIRM("CONFIRM", new ConfirmCommand()),
    DEFAULT("DEFAULT", new DefaultCommand());

    private final String name;
    private final Command command;
    private static final Logger LOGGER = LogManager.getLogger(CommandType.class);

    CommandType(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public Command getCommand() {
        return command;
    }

    public static Command define(String commandStr) {
        if (commandStr == null || commandStr.isBlank()) {
            LOGGER.warn("Empty command received, returning DEFAULT");
            return DEFAULT.command;
        }

        return findCommand(commandStr)
                .orElseGet(() -> {
                    LOGGER.error("Invalid command: '{}', returning DEFAULT command", commandStr);
                    return DEFAULT.command;
                });
    }

    private static Optional<Command> findCommand(String commandName) {
        return Arrays.stream(CommandType.values())
                .filter(command -> command.getName().equalsIgnoreCase(commandName))
                .findFirst()
                .map(CommandType::getCommand);
    }
}