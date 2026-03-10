package com.example.demo.dao.impl;

import com.example.demo.dao.BaseDao;
import com.example.demo.dao.UserDao;
import com.example.demo.db.DatabaseConnector;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends BaseDao<User> implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

    private static final String SELECT_ALL = "SELECT id, login, password FROM users";
    private static final String SELECT_BY_LOGIN = "SELECT id, login, password FROM users WHERE login = ?";
    private static final String SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM users WHERE login = ?";
    private static final String INSERT = "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE users SET login = ?, password = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";

    @Override
    public boolean insert(User user) throws DataException {
        LOGGER.debug("Inserting user: {}", user.getLogin());

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            int affectedRows = statement.executeUpdate();
            LOGGER.debug("Insert affected rows: {}", affectedRows);

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        LOGGER.info("User inserted successfully with ID: {}", user.getId());
                    }
                }
                return true;
            }

            return false;

        } catch (SQLException e) {
            LOGGER.error("SQL Error inserting user: " + user.getLogin(), e);
            throw new DataException("Failed to insert user: " + user.getLogin(), e);
        }
    }

    @Override
    public boolean delete(User user) throws DataException {
        if (user == null) {
            throw new DataException("Cannot delete null user");
        }

        LOGGER.debug("Attempting to delete user with ID: {}", user.getId());

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {

            statement.setInt(1, user.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("User deleted successfully with ID: {}", user.getId());
                return true;
            } else {
                LOGGER.warn("No user found with ID: {} to delete", user.getId());
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("SQL Error deleting user with ID: " + user.getId(), e);
            throw new DataException("Failed to delete user with ID: " + user.getId(), e);
        }
    }

    @Override
    public List<User> findAll() throws DataException {
        List<User> users = new ArrayList<>();
        LOGGER.debug("Fetching all users from database");

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(COLUMN_ID));
                user.setLogin(resultSet.getString(COLUMN_LOGIN));
                user.setPassword(resultSet.getString(COLUMN_PASSWORD));
                users.add(user);
            }

            LOGGER.debug("Found {} users in database", users.size());
            return users.isEmpty() ? Collections.emptyList() : users;

        } catch (SQLException e) {
            LOGGER.error("SQL Error while fetching all users", e);
            throw new DataException("Failed to fetch all users", e);
        }
    }

    @Override
    public User update(User user) throws DataException {
        if (user == null) {
            throw new DataException("Cannot update null user");
        }

        LOGGER.debug("Attempting to update user with ID: {}", user.getId());

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.info("User updated successfully with ID: {}", user.getId());
                return user;
            } else {
                LOGGER.warn("No user found with ID: {} to update", user.getId());
                throw new DataException("User not found with ID: " + user.getId());
            }

        } catch (SQLException e) {
            LOGGER.error("SQL Error while updating user with ID: " + user.getId(), e);
            throw new DataException("Failed to update user with ID: " + user.getId(), e);
        }
    }

    @Override
    public boolean authenticate(String login, String password) throws DataException {
        if (login == null || login.trim().isEmpty()) {
            LOGGER.warn("Authentication attempt with empty login");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            LOGGER.warn("Authentication attempt with empty password for user: {}", login);
            return false;
        }

        LOGGER.info("Authentication attempt for user: {}", login);

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PASSWORD_BY_LOGIN)) {

            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString(COLUMN_PASSWORD);
                    boolean isAuthenticated = PasswordUtil.checkPassword(password, hashedPassword);

                    if (isAuthenticated) {
                        LOGGER.info("User {} authenticated successfully", login);
                    } else {
                        LOGGER.warn("Invalid password for user: {}", login);
                    }

                    return isAuthenticated;
                } else {
                    LOGGER.warn("User not found: {}", login);
                    return false;
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQL Error during authentication for user: " + login, e);
            throw new DataException("Authentication failed for user: " + login, e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws DataException {
        if (login == null || login.trim().isEmpty()) {
            LOGGER.warn("Attempt to find user with empty login");
            return Optional.empty();
        }

        LOGGER.debug("Finding user by login: {}", login);

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN)) {

            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt(COLUMN_ID));
                    user.setLogin(resultSet.getString(COLUMN_LOGIN));
                    user.setPassword(resultSet.getString(COLUMN_PASSWORD));
                    LOGGER.debug("User found with ID: {}", user.getId());
                    return Optional.of(user);
                }

                LOGGER.debug("User not found: {}", login);
                return Optional.empty();
            }

        } catch (SQLException e) {
            LOGGER.error("SQL Error while finding user by login: " + login, e);
            throw new DataException("Failed to find user by login: " + login, e);
        }
    }
}