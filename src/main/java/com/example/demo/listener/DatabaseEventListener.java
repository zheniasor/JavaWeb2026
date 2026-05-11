package com.example.demo.listener;

import com.example.demo.event.DatabaseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseEventListener implements java.util.EventListener {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseEventListener.class);

    public void onDatabaseEvent(DatabaseEvent event) {
        LOGGER.debug("Database pool event: operation={}, active={}, free={}",
                event.getOperation(),
                event.getActiveConnections(),
                event.getFreeConnections());

        if (event.getFreeConnections() == 0 && event.getActiveConnections() > 0) {
            LOGGER.warn("Connection pool is exhausted! active={}, free={}",
                    event.getActiveConnections(), event.getFreeConnections());
        }
    }
}