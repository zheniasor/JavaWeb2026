package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ConfirmCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ConfirmCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        String token = request.getParameter(ParameterConstants.TOKEN_PARAM);

        if (token == null || token.isBlank()) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Неверная ссылка для подтверждения");
            return PageConstants.INDEX_PAGE;
        }

        UserService userService = UserServiceImpl.getInstance();

        try {
            Optional<User> userOpt = userService.findByToken(token);

            if (userOpt.isEmpty()) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Недействительная или просроченная ссылка для подтверждения");
                return PageConstants.INDEX_PAGE;
            }

            User user = userOpt.get();
            boolean confirmed = userService.confirmUser(user.getId());

            if (confirmed) {
                request.setAttribute(AttributeConstants.MESSAGE_ATTR,
                        "Электронная почта успешно подтверждена! Теперь вы можете войти в систему.");
                LOGGER.info("User {} confirmed email successfully", user.getLogin());
            } else {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR,
                        "Ошибка подтверждения электронной почты");
                LOGGER.warn("Failed to confirm user: {}", user.getLogin());
            }
        } catch (DataException e) {
            LOGGER.error("Error during email confirmation", e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR,
                    "Ошибка сервера во время подтверждения");
        }

        return PageConstants.INDEX_PAGE;
    }
}