package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(ParameterConstants.LOGIN_PARAM);
        String password = request.getParameter(ParameterConstants.PASSWORD_PARAM);

        LOGGER.info("Login attempt for user: {}", login);

        UserService userService = UserServiceImpl.getInstance();
        String page;

        try {
            if (userService.authenticate(login, password)) {
                request.setAttribute(AttributeConstants.USER_ATTR, login);
                page = PageConstants.MAIN_PAGE;
                LOGGER.info("User {} logged in successfully", login);
            } else {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Incorrect login or password");
                page = PageConstants.INDEX_PAGE;
                LOGGER.warn("Failed login attempt for user: {}", login);
            }
        } catch (DataException e) {
            LOGGER.error("Database error during login for user: " + login, e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Database error occurred. Please try again later.");
            page = PageConstants.INDEX_PAGE;
        }

        return page;
    }
}