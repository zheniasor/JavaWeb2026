package com.example.demo.listener;

import com.example.demo.db.ConnectionPool;
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
        LOGGER.info("Application starting");

        ConnectionPool.PoolStats stats = ConnectionPool.getInstance().getStats();
        LOGGER.info("Initial pool stats: active={}, idle={}, total={}, waiting={}",
                stats.active(), stats.idle(), stats.total(), stats.waiting());

        LOGGER.info("Application started successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application stopping");

        ConnectionPool.PoolStats stats = ConnectionPool.getInstance().getStats();
        LOGGER.info("Final pool stats: active={}, idle={}, total={}, waiting={}",
                stats.active(), stats.idle(), stats.total(), stats.waiting());

        ConnectionPool.getInstance().shutdown();

        LOGGER.info("Application stopped cleanly");
    }
}