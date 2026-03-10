package com.example.demo.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        return ConnectionPoolManager.getConnection();
    }

    public static void close() {
        ConnectionPoolManager.shutdown();
    }
}