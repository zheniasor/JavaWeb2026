package com.example.demo.listener;

import com.example.demo.db.ConnectionPool;
import com.example.demo.exception.DataException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Application starting");

        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            LOGGER.info("Connection pool initialized successfully");
            LOGGER.info("Pool size: 10, Min idle: 2");

        } catch (DataException e) {
            LOGGER.error("Failed to initialize connection pool", e);
        }

        LOGGER.info("Application started successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application stopping");

        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            pool.close();
            LOGGER.info("Connection pool closed successfully");
        } catch (DataException e) {
            LOGGER.error("Error closing connection pool", e);
        }

        LOGGER.info("Application stopped cleanly");
    }
}