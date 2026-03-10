package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddUserCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddUserCommand.class);

    private static final String REGISTER_PAGE = "pages/register.jsp";
    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "pass";
    private static final String MESSAGE_ATTR = "message";
    private static final String SUCCESS_MESSAGE = "User %s added successfully!";
    private static final String ERROR_LOGIN_EMPTY = "Login cannot be empty";
    private static final String ERROR_PASSWORD_EMPTY = "Password cannot be empty";
    private static final String ERROR_LOGIN_TOO_SHORT = "Login must be at least 3 characters";
    private static final String ERROR_LOGIN_TOO_LONG = "Login must not exceed 20 characters";
    private static final String ERROR_PASSWORD_TOO_SHORT = "Password must be at least 3 characters";
    private static final String ERROR_PASSWORD_TOO_LONG = "Password must not exceed 50 characters";
    private static final String ERROR_USER_EXISTS = "User with this login already exists";
    private static final String ERROR_INVALID_CHARACTERS = "Login contains invalid characters (only letters, numbers and underscore allowed)";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String ERROR_MESSAGE_GENERAL = "Registration failed";

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);

        LOGGER.info("Registration attempt for user: {}", login);

        UserService userService = UserServiceImpl.getInstance();
        User user = getUser(login, password);

        try {
            boolean registered = userService.register(user);

            if (registered) {
                request.setAttribute(MESSAGE_ATTR, String.format(SUCCESS_MESSAGE, login));
                LOGGER.info("User {} registered successfully", login);
            } else {
                if (login == null || login.trim().isEmpty()) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_LOGIN_EMPTY);
                } else if (password == null || password.trim().isEmpty()) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_PASSWORD_EMPTY);
                } else if (login.length() < 3) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_LOGIN_TOO_SHORT);
                } else if (login.length() > 20) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_LOGIN_TOO_LONG);
                } else if (password.length() < 3) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_PASSWORD_TOO_SHORT);
                } else if (password.length() > 50) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_PASSWORD_TOO_LONG);
                } else if (!login.matches("^[a-zA-Z0-9_]+$")) {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_INVALID_CHARACTERS);
                } else {
                    request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_USER_EXISTS);
                }

                LOGGER.warn("Registration failed for user: {}", login);
            }
        } catch (Exception e) {
            request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_MESSAGE_GENERAL);
            LOGGER.error("Error during registration for user: " + login, e);
        }

        return REGISTER_PAGE;
    }

    private static User getUser(String login, String password) {
        return new User(login, password);
    }
}