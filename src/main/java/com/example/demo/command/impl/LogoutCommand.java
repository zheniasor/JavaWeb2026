package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String user = (String) session.getAttribute(AttributeConstants.USER_ATTR);
            session.invalidate();
            LOGGER.info("User {} logged out", user);
        } else {
            LOGGER.debug("Logout attempted with no active session");
        }

        return PageConstants.INDEX_PAGE;
    }
}