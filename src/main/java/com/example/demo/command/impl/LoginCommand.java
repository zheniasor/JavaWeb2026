package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import com.example.demo.entity.User;
import com.example.demo.event.UserEvent;
import com.example.demo.exception.DataException;
import com.example.demo.listener.UserEventListener;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    private final UserService userService;

    // Конструктор для production (использует реальный сервис)
    public LoginCommand() {
        this.userService = UserServiceImpl.getInstance();
    }

    // Конструктор для тестов (позволяет внедрить mock)
    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(ParameterConstants.LOGIN_PARAM);
        String password = request.getParameter(ParameterConstants.PASSWORD_PARAM);

        String page = PageConstants.INDEX_PAGE;

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Заполните все поля");
            return page;
        }

        try {
            if (userService.authenticate(login, password)) {
                String escapedLogin = StringEscapeUtils.escapeHtml4(login);
                HttpSession session = request.getSession();
                session.setAttribute(AttributeConstants.USER_ATTR, escapedLogin);

                Optional<User> userOpt = userService.findByLogin(login);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    session.setAttribute("userRole", user.getRole());
                    session.setAttribute("userAvatar", user.getAvatarPath());

                    UserEvent event = new UserEvent(this, user, "LOGIN");
                    UserEventListener userEventListener = new UserEventListener();
                    userEventListener.onUserAction(event);
                }

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