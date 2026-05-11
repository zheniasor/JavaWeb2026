package com.example.demo.listener;

import com.example.demo.event.UserEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserEventListener implements java.util.EventListener {

    private static final Logger LOGGER = LogManager.getLogger(UserEventListener.class);

    public void onUserAction(UserEvent event) {
        LOGGER.info("User action: action={}, user={}, time={}",
                event.getAction(),
                event.getUser() != null ? event.getUser().getLogin() : "unknown",
                event.getTimestamp());
    }
}