package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AdminPanelCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AdminPanelCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(AttributeConstants.USER_ATTR) == null) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Пожалуйста, сначала войдите в систему");
            return PageConstants.INDEX_PAGE;
        }

        String currentUser = (String) session.getAttribute(AttributeConstants.USER_ATTR);
        UserService userService = UserServiceImpl.getInstance();

        try {
            Optional<User> currentUserOpt = userService.findByLogin(currentUser);
            if (currentUserOpt.isEmpty() || !currentUserOpt.get().isAdmin()) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Доступ запрещен. Требуется учетная запись администратора.");
                return PageConstants.INDEX_PAGE;
            }

            List<User> allUsers = userService.findAll();
            request.setAttribute("users", allUsers);
            LOGGER.info("Admin {} accessed admin panel", currentUser);

        } catch (DataException e) {
            LOGGER.error("Error loading admin panel", e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Ошибка сервера");
            return PageConstants.MAIN_PAGE;
        }

        return PageConstants.ADMIN_PANEL_PAGE;
    }
}