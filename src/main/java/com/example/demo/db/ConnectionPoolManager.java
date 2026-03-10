package com.example.demo.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class ConnectionPoolManager {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolManager.class);

    private static final String DB_URL = """
        jdbc:sqlserver://ZHENIA;
        instanceName=SQLEXPRESS02;
        databaseName=UserManagementDB;
        encrypt=true;
        trustServerCertificate=true;""";
    private static final String DB_USER = "app_user";
    private static final String DB_PASSWORD = "123456";
    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final int POOL_SIZE = 20;
    private static final int MIN_IDLE = 5;
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int IDLE_TIMEOUT = 600000;
    private static final int MAX_LIFETIME = 1800000;

    private static HikariDataSource dataSource;
    private static boolean isShutdown = false;

    static {
        initializePool();
    }

    private static void initializePool() {
        try {
            LOGGER.info("Initializing Connection Pool...");

            Class.forName(DB_DRIVER);
            LOGGER.info("Driver loaded: {}", DB_DRIVER);

            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(DB_URL);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASSWORD);
            config.setDriverClassName(DB_DRIVER);

            config.setMaximumPoolSize(POOL_SIZE);
            config.setMinimumIdle(MIN_IDLE);
            config.setConnectionTimeout(CONNECTION_TIMEOUT);
            config.setIdleTimeout(IDLE_TIMEOUT);
            config.setMaxLifetime(MAX_LIFETIME);

            config.setConnectionTestQuery("SELECT 1");
            config.setValidationTimeout(5000);

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            config.setAutoCommit(false);

            dataSource = new HikariDataSource(config);

            LOGGER.info("Connection Pool initialized successfully");
            LOGGER.info("   Pool size: {}, Min idle: {}", POOL_SIZE, MIN_IDLE);

            testConnection();

        } catch (ClassNotFoundException e) {
            LOGGER.fatal("FATAL: Database driver not found!", e);
            throw new RuntimeException("Driver not found", e);
        } catch (Exception e) {
            LOGGER.fatal("FATAL: Failed to initialize connection pool", e);
            throw new RuntimeException("Connection pool initialization failed", e);
        }
    }

    private static void testConnection() {
        try (Connection conn = getConnection()) {
            LOGGER.info("Test connection successful");
        } catch (SQLException e) {
            LOGGER.error("Test connection failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (isShutdown) {
            throw new SQLException("Connection pool is shut down");
        }

        try {
            Connection connection = dataSource.getConnection();
            LOGGER.debug("Connection obtained. Active: {}, Idle: {}",
                    dataSource.getHikariPoolMXBean().getActiveConnections(),
                    dataSource.getHikariPoolMXBean().getIdleConnections());
            return connection;
        } catch (SQLException e) {
            LOGGER.error("Failed to get connection from pool", e);
            throw e;
        }
    }

   public static void shutdown() {
        LOGGER.info("Shutting down Connection Pool...");
        isShutdown = true;

        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("DataSource closed");
        }

        deregisterDrivers();

        LOGGER.info("Connection Pool shutdown complete");
    }

    private static void deregisterDrivers() {
        LOGGER.info("Deregistering JDBC drivers...");

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();

            if (driver.getClass().getClassLoader() == ConnectionPoolManager.class.getClassLoader()) {
                try {
                    DriverManager.deregisterDriver(driver);
                    LOGGER.info("Deregistered driver: {}", driver.getClass().getName());
                } catch (SQLException e) {
                    LOGGER.error("Failed to deregister driver: {}", driver.getClass().getName(), e);
                }
            } else {
                LOGGER.debug("Driver {} belongs to system - skipping", driver.getClass().getName());
            }
        }
    }

    public static PoolStats getStats() {
        if (dataSource == null || dataSource.isClosed()) {
            return new PoolStats(0, 0, 0, 0);
        }

        var mxBean = dataSource.getHikariPoolMXBean();
        return new PoolStats(
                mxBean.getActiveConnections(),
                mxBean.getIdleConnections(),
                mxBean.getTotalConnections(),
                mxBean.getThreadsAwaitingConnection()
        );
    }

    public record PoolStats(int active, int idle, int total, int waiting) {
        @Override
        public String toString() {
            return String.format("Active: %d, Idle: %d, Total: %d, Waiting: %d",
                    active, idle, total, waiting);
        }
    }
}