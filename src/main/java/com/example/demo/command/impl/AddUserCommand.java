package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.constants.PageConstants;
import com.example.demo.constants.AttributeConstants;
import com.example.demo.constants.ParameterConstants;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddUserCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddUserCommand.class);

    private static final String LOGIN_PATTERN = "^[a-zA-Z0-9_]+$";
    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 3;
    private static final int MAX_PASSWORD_LENGTH = 50;

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(ParameterConstants.LOGIN_PARAM);
        String password = request.getParameter(ParameterConstants.PASSWORD_PARAM);

        LOGGER.info("Registration attempt for user: {}", login);

        if (login == null || login.strip().isBlank() ||
                password == null || password.strip().isBlank() ||
                login.length() <  MIN_LOGIN_LENGTH || login.length() > MAX_LOGIN_LENGTH ||
                password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH ||
                !login.matches(LOGIN_PATTERN)) {

            request.setAttribute("errorMessage", "Registration failed");
            LOGGER.warn("Validation failed for user: {}", login);
            return PageConstants.REGISTER_PAGE;
        }

        UserService userService = UserServiceImpl.getInstance();
        User user = new User(login, password);

        try {
            boolean registered = userService.register(user);

            if (registered) {
                request.setAttribute(AttributeConstants.MESSAGE_ATTR, String.format("User %s added successfully!", login));
                LOGGER.info("User {} registered successfully", login);
            } else {
                request.setAttribute("errorMessage", "User with this login already exists");
                LOGGER.warn("User already exists: {}", login);
            }
        } catch (DataException e) {
            request.setAttribute("errorMessage", "Registration failed");
            LOGGER.error("Error during registration for user: " + login, e);
        }

        return PageConstants.REGISTER_PAGE;
    }
}