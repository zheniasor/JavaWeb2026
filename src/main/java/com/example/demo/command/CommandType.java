package com.example.demo.command;

import com.example.demo.command.impl.AddUserCommand;
import com.example.demo.command.impl.DefaultCommand;
import com.example.demo.command.impl.LoginCommand;
import com.example.demo.command.impl.LogoutCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum CommandType {
    ADD_USER(new AddUserCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand());

    Command command;
    private static final Logger LOGGER = LogManager.getLogger(CommandType.class);

    CommandType(Command command) {
        this.command = command;
    }

    public static Command define(String commandStr) {
        try {
            if (commandStr == null || commandStr.trim().isEmpty()) {
                LOGGER.warn("Empty command received, returning DEFAULT");
                return DEFAULT.command;
            }

            CommandType current = CommandType.valueOf(commandStr.toUpperCase());
            LOGGER.debug("Command resolved: {}", commandStr);
            return current.command;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid command: '{}', returning DEFAULT command", commandStr);
            return DEFAULT.command;
        } catch (Exception e) {
            LOGGER.error("Unexpected error resolving command: '{}'", commandStr, e);
            return DEFAULT.command;
        }
    }
}