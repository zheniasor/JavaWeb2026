package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(ParameterConstants.LOGIN_PARAM);
        String password = request.getParameter(ParameterConstants.PASSWORD_PARAM);

        String page = PageConstants.INDEX_PAGE;

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Заполните все поля");
            return page;
        }

        UserService userService = UserServiceImpl.getInstance();
        try {
            if (userService.authenticate(login, password)) {
                String escapedLogin = StringEscapeUtils.escapeHtml4(login);
                request.getSession().setAttribute(AttributeConstants.USER_ATTR, escapedLogin);
                page = PageConstants.MAIN_PAGE;
            } else {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Неверный логин или пароль");
            }
        } catch (DataException e) {
            LOGGER.error("Database error during login for user: {}", login, e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Ошибка сервера");
        }
        return page;
    }
}