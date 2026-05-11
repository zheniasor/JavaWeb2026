package com.example.demo.event;

import java.time.LocalDateTime;
import java.util.EventObject;

public class DatabaseEvent extends EventObject {

    private final LocalDateTime timestamp;
    private final String operation;
    private final int activeConnections;
    private final int freeConnections;

    public DatabaseEvent(Object source, String operation, int activeConnections, int freeConnections) {
        super(source);
        this.timestamp = LocalDateTime.now();
        this.operation = operation;
        this.activeConnections = activeConnections;
        this.freeConnections = freeConnections;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getOperation() { return operation; }
    public int getActiveConnections() { return activeConnections; }
    public int getFreeConnections() { return freeConnections; }
}