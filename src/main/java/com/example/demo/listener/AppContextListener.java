package com.example.demo.listener;

import com.example.demo.db.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("🚀 Application starting...");

        LOGGER.info("Application started successfully");

        LOGGER.info("Initial pool stats: {}", ConnectionPoolManager.getStats());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application stopping...");

        LOGGER.info("Final pool stats: {}", ConnectionPoolManager.getStats());

        ConnectionPoolManager.shutdown();

        LOGGER.info("Application stopped cleanly");
    }
}