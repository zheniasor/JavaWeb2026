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
        Command resultCommand = DEFAULT.command;
        try {
            if (commandStr == null || commandStr.strip().isBlank()) {
                LOGGER.warn("Empty command received, returning DEFAULT");
                return resultCommand;
            }

            CommandType current = CommandType.valueOf(commandStr.toUpperCase());
            LOGGER.debug("Command resolved: {}", commandStr);
            resultCommand = current.command;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid command: '{}', returning DEFAULT command", commandStr);
        }
       return resultCommand;
    }
}