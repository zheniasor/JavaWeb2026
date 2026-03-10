package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    private static final String MAIN_PAGE = "pages/main.jsp";
    private static final String INDEX_PAGE = "index.jsp";
    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "pass";
    private static final String USER_ATTR = "user";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String ERROR_MESSAGE = "Incorrect login or password";
    private static final String DATABASE_ERROR = "Database error occurred. Please try again later.";

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);

        LOGGER.info("Login attempt for user: {}", login);

        UserService userService = UserServiceImpl.getInstance();
        String page;

        try {
            if (userService.authenticate(login, password)) {
                request.setAttribute(USER_ATTR, login);
                page = MAIN_PAGE;
                LOGGER.info("User {} logged in successfully", login);
            } else {
                request.setAttribute(ERROR_MESSAGE_ATTR, ERROR_MESSAGE);
                page = INDEX_PAGE;
                LOGGER.warn("Failed login attempt for user: {}", login);
            }
        } catch (DataException e) {
            LOGGER.error("Database error during login for user: " + login, e);
            request.setAttribute(ERROR_MESSAGE_ATTR, DATABASE_ERROR);
            page = INDEX_PAGE;
        }

        return page;
    }
}