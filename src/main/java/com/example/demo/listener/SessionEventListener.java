package com.example.demo.listener;

import com.example.demo.event.UserEvent;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionEventListener implements HttpSessionListener {

    private static final Logger LOGGER = LogManager.getLogger(SessionEventListener.class);
    private static final UserEventListener userEventListener = new UserEventListener();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        LOGGER.debug("Session created: id={}", session.getId());

        session.setMaxInactiveInterval(30 * 60);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String userId = (String) session.getAttribute("user");
        LOGGER.debug("Session destroyed: id={}, user={}", session.getId(), userId);

        if (userId != null) {
            UserEvent event = new UserEvent(this, null, "SESSION_TIMEOUT");
            userEventListener.onUserAction(event);
        }
    }
}