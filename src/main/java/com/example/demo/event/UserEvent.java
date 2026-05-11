package com.example.demo.event;

import com.example.demo.entity.User;
import java.time.LocalDateTime;
import java.util.EventObject;

public class UserEvent extends EventObject {

    private final User user;
    private final LocalDateTime timestamp;
    private final String action;

    public UserEvent(Object source, User user, String action) {
        super(source);
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.action = action;
    }

    public User getUser() { return user; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getAction() { return action; }
}