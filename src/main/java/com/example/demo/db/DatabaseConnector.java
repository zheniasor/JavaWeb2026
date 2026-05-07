package com.example.demo.db;

import com.example.demo.exception.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnector.class);
    private static ConnectionPool connectionPool;

    static {
        try {
            connectionPool = ConnectionPool.getInstance();
            LOGGER.info("DatabaseConnector initialized");
        } catch (DataException e) {
            LOGGER.fatal("Failed to initialize connection pool", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return connectionPool.getConnection();
        } catch (RuntimeException e) {
            throw new SQLException("Failed to get connection from pool", e);
        }
    }

    public static void close() {
        if (connectionPool != null) {
            connectionPool.close();
        }
    }
}