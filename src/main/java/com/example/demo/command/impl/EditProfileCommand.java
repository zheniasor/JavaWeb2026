package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.validator.UserValidator;
import com.example.demo.validator.impl.UserValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class EditProfileCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(EditProfileCommand.class);
    private final UserValidator userValidator = new UserValidatorImpl();

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(AttributeConstants.USER_ATTR) == null) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Пожалуйста, сначала войдите в систему");
            return PageConstants.INDEX_PAGE;
        }

        String currentLogin = (String) session.getAttribute(AttributeConstants.USER_ATTR);
        String newLogin = request.getParameter(ParameterConstants.NEW_LOGIN_PARAM);
        String newPassword = request.getParameter(ParameterConstants.NEW_PASSWORD_PARAM);
        String newEmail = request.getParameter(ParameterConstants.EMAIL_PARAM);

        String page = PageConstants.MAIN_PAGE;

        UserService userService = UserServiceImpl.getInstance();

        try {
            Optional<User> userOpt = userService.findByLogin(currentLogin);
            if (userOpt.isEmpty()) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Пользователь не найден");
                return PageConstants.INDEX_PAGE;
            }

            User user = userOpt.get();

            boolean hasChanges = false;

            if (newLogin != null && !newLogin.isBlank() && !newLogin.equals(currentLogin)) {
                if (isValidLogin(newLogin, userService)) {
                    user.setLogin(newLogin);
                    session.setAttribute(AttributeConstants.USER_ATTR, newLogin);
                    hasChanges = true;
                } else {
                    request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Неверный логин или уже существует");
                    return getEditPage(page);
                }
            }

            if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
                if (isValidEmail(newEmail, userService)) {
                    user.setEmail(newEmail);
                    hasChanges = true;
                } else {
                    request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Неверный email или уже существует");
                    return getEditPage(page);
                }
            }

            if (newPassword != null && !newPassword.isBlank()) {
                User tempUser = new User(user.getLogin(), newPassword, user.getEmail());
                Map<String, String> errors = userValidator.validate(tempUser);
                if (errors.isEmpty()) {
                    user.setPassword(newPassword);
                    hasChanges = true;
                } else {
                    request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR,
                            errors.getOrDefault("password", "Неверный пароль"));
                    return getEditPage(page);
                }
            }

            if (hasChanges) {
                User updated = userService.update(user);
                if (updated != null) {
                    request.setAttribute(AttributeConstants.MESSAGE_ATTR, "Профиль успешно обновлен!");
                    LOGGER.info("User {} updated profile", currentLogin);
                } else {
                    request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Не удалось обновить профиль");
                }
            } else {
                request.setAttribute(AttributeConstants.MESSAGE_ATTR, "Изменений не обнаружено");
            }

        } catch (DataException e) {
            LOGGER.error("Error updating profile for user: {}", currentLogin, e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Ошибка сервера");
        }

        return page;
    }

    private String getEditPage(String page) {
        return page.equals(PageConstants.MAIN_PAGE) ? PageConstants.EDIT_PROFILE_PAGE : page;
    }

    private boolean isValidLogin(String login, UserService userService) throws DataException {
        Optional<User> existing = userService.findByLogin(login);
        return existing.isEmpty();
    }

    private boolean isValidEmail(String email, UserService userService) throws DataException {
        Optional<User> existing = userService.findByEmail(email);
        return existing.isEmpty();
    }
}