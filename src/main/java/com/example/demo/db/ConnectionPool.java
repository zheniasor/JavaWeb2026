package com.example.demo.db;

import com.example.demo.exception.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ConnectionPool implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;

    private static final String URL = "jdbc:sqlserver://ZHENIA;instanceName=SQLEXPRESS02;databaseName=UserManagementDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "app_user";
    private static final String PASSWORD = "123456";
    private static final int POOL_SIZE = 10;

    private final Queue<Connection> freeConnections = new ArrayDeque<>();
    private int activeConnections = 0;

    private ConnectionPool() throws DataException {
        initializePool();
    }

    public static synchronized ConnectionPool getInstance() throws DataException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private void initializePool() throws DataException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            for (int i = 0; i < POOL_SIZE; i++) {
                freeConnections.add(createConnection());
            }
            LOGGER.info("Connection pool initialized with {} connections", POOL_SIZE);
        } catch (Exception e) {
            throw new DataException("Failed to initialize connection pool", e);
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public synchronized Connection getConnection() {
        while (freeConnections.isEmpty() && activeConnections >= POOL_SIZE) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted waiting for connection", e);
            }
        }

        Connection conn = freeConnections.poll();
        activeConnections++;
        LOGGER.debug("Connection obtained. Active: {}, Free: {}", activeConnections, freeConnections.size());

        // Возвращаем Proxy-обертку
        return new ConnectionProxy(conn, this);
    }

    synchronized void releaseConnection(Connection connection) {
        freeConnections.add(connection);
        activeConnections--;
        LOGGER.debug("Connection released. Active: {}, Free: {}", activeConnections, freeConnections.size());
        notifyAll();
    }

    @Override
    public synchronized void close() {
        for (Connection conn : freeConnections) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("Error closing connection", e);
            }
        }
        freeConnections.clear();
        activeConnections = 0;
        LOGGER.info("Connection pool closed");
    }
}