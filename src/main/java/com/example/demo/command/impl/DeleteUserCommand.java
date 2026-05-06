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
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteUserCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(DeleteUserCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(AttributeConstants.USER_ATTR) == null) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Пожалуйста, сначала войдите в систему");
            return PageConstants.INDEX_PAGE;
        }

        String currentUser = (String) session.getAttribute(AttributeConstants.USER_ATTR);

        String userIdParam = request.getParameter(ParameterConstants.USER_ID_PARAM);
        if (userIdParam == null || userIdParam.isBlank()) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Требуется идентификатор пользователя");
            return getRedirectPage(request);
        }

        int userIdToDelete;
        try {
            userIdToDelete = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Неверный идентификатор пользователя");
            return getRedirectPage(request);
        }

        UserService userService = UserServiceImpl.getInstance();

        try {
            Optional<User> currentUserOpt = userService.findByLogin(currentUser);
            if (currentUserOpt.isEmpty() || !currentUserOpt.get().isAdmin()) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Доступ запрещен. Требуется учетная запись администратора.");
                return PageConstants.INDEX_PAGE;
            }

             if (currentUserOpt.get().getId() == userIdToDelete) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Не удается удалить вашу собственную учетную запись");
                return getRedirectPage(request);
            }

            boolean deleted = userService.deleteUser(userIdToDelete);

            if (deleted) {
                request.setAttribute(AttributeConstants.MESSAGE_ATTR, "Пользователь успешно удален");
                LOGGER.info("Administrator {} deleted user with ID: {}", currentUser, userIdToDelete);
            } else {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Не удалось удалить пользователя");
            }

        } catch (DataException e) {
            LOGGER.error("Error deleting user with ID: {}", userIdToDelete, e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Ошибка сервера");
        }

        return getRedirectPage(request);
    }

    private String getRedirectPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("admin")) {
            return PageConstants.ADMIN_PANEL_PAGE;
        }
        return PageConstants.MAIN_PAGE;
    }
}