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
import com.example.demo.validator.UserValidator;
import com.example.demo.validator.impl.UserValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class AddUserCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AddUserCommand.class);
    private final UserValidator userValidator = new UserValidatorImpl();

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(ParameterConstants.LOGIN_PARAM);
        String password = request.getParameter(ParameterConstants.PASSWORD_PARAM);
        String email = request.getParameter(ParameterConstants.EMAIL_PARAM);

        String page = PageConstants.INDEX_PAGE;
        User user = new User(login, password, email);

        Map<String, String> errors = userValidator.validate(user);

        if (!errors.isEmpty()) {
            request.setAttribute(AttributeConstants.ERRORS, errors);
            request.setAttribute("loginExample", "user123");
            LOGGER.warn("Validation failed for user: {}", login);
            return PageConstants.REGISTER_PAGE;
        }

        UserService userService = UserServiceImpl.getInstance();
        try {
            boolean registered = userService.register(user);

            if (registered) {
                UserEvent event = new UserEvent(this, user, "REGISTER");
                UserEventListener userEventListener = new UserEventListener();
                userEventListener.onUserAction(event);
                request.setAttribute("message", "На указанный email отправлено письмо с подтверждением. Пожалуйста, проверьте почту.");
                LOGGER.info("User registered successfully, confirmation email sent: {}", login);
                return PageConstants.CONFIRMATION_PAGE;
            } else {
                request.setAttribute("errorMessage", "Пользователь с таким логином или email уже существует");
                page = PageConstants.REGISTER_PAGE;
                LOGGER.warn("User already exists: {}", login);
            }
        } catch (DataException e) {
            LOGGER.error("Error during registration for user: {}", login, e);
            request.setAttribute("errorMessage", "Регистрация не удалась из-за ошибки сервера");
            page = PageConstants.REGISTER_PAGE;
        }

        return page;
    }
}