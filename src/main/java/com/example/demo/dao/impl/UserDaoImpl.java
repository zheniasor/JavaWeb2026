package com.example.demo.dao.impl;

import com.example.demo.dao.ColumnName;
import com.example.demo.dao.BaseDao;
import com.example.demo.dao.UserDao;
import com.example.demo.db.ConnectionPool;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.PasswordEncoder;
import com.example.demo.mapper.impl.UserMapperImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends BaseDao<User> implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);
    private final UserMapper userMapper = new UserMapperImpl();

    private static final String SELECT_ALL = "SELECT id, login, password, email, confirmed FROM users";
    private static final String SELECT_BY_LOGIN = "SELECT id, login, password, email, confirmed FROM users WHERE login = ?";
    private static final String SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM users WHERE login = ?";
    private static final String INSERT_USER =
            "INSERT INTO users (login, password, email, confirmation_token, confirmed) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET login = ?, password = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_BY_EMAIL = "SELECT id, login, password, email, confirmed FROM users WHERE email = ?";
    private static final String SELECT_BY_TOKEN = "SELECT id, login, password, email, confirmed FROM users WHERE confirmation_token = ?";
    private static final String UPDATE_CONFIRMED = "UPDATE users SET confirmed = 1, confirmation_token = NULL WHERE id = ?";

    @Override
    public boolean insert(User user) throws DataException {
        LOGGER.debug("Inserting user: {}", user.getLogin());

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getConfirmationToken());
            statement.setBoolean(5, user.isConfirmed());

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

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {

            statement.setInt(1, user.getId());
            int affectedRows = statement.executeUpdate();

            LOGGER.info("Delete operation for user ID: {} - {} rows affected", user.getId(), affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.error("SQL Error deleting user with ID: " + user.getId(), e);
            throw new DataException("Failed to delete user with ID: " + user.getId(), e);
        }
    }

    @Override
    public List<User> findAll() throws DataException {
        List<User> users = new ArrayList<>();
        LOGGER.debug("Fetching all users from database");

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            while (resultSet.next()) {
                users.add(userMapper.mapRow(resultSet));
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

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {

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
        if (login == null || login.isBlank()) {
            LOGGER.warn("Authentication attempt with empty login");
            return false;
        }

        if (password == null || password.isBlank()) {
            LOGGER.warn("Authentication attempt with empty password for user: {}", login);
            return false;
        }

        LOGGER.info("Authentication attempt for user: {}", login);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PASSWORD_BY_LOGIN)) {

            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString(ColumnName.PASSWORD);
                    boolean isAuthenticated = PasswordEncoder.matches(password, hashedPassword);

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
        if (login == null || login.isBlank()) {
            LOGGER.warn("Attempt to find user with empty login");
            return Optional.empty();
        }

        LOGGER.debug("Finding user by login: {}", login);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN)) {

            statement.setString(1, login);
            LOGGER.debug("SQL: {}", SELECT_BY_LOGIN);
            LOGGER.debug("Parameter: login={}", login);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = userMapper.mapRow(resultSet);
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

    @Override
    public Optional<User> findByEmail(String email) throws DataException {
        if (email == null || email.isBlank()) {
            LOGGER.warn("Attempt to find user with empty email");
            return Optional.empty();
        }

        LOGGER.debug("Finding user by email: {}", email);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = userMapper.mapRow(resultSet);
                    LOGGER.debug("User found with ID: {}", user.getId());
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Error while finding user by email: " + email, e);
            throw new DataException("Failed to find user by email: " + email, e);
        }
    }

    @Override
    public Optional<User> findByToken(String token) throws DataException {
        if (token == null || token.isBlank()) {
            LOGGER.warn("Attempt to find user with empty token");
            return Optional.empty();
        }

        LOGGER.debug("Finding user by token: {}", token);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_TOKEN)) {

            statement.setString(1, token);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = userMapper.mapRow(resultSet);
                    LOGGER.debug("User found with ID: {}", user.getId());
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Error while finding user by token", e);
            throw new DataException("Failed to find user by token", e);
        }
    }

    @Override
    public boolean confirmUser(int userId) throws DataException {
        LOGGER.debug("Confirming user with ID: {}", userId);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CONFIRMED)) {

            statement.setInt(1, userId);

            LOGGER.debug("SQL: {}", UPDATE_CONFIRMED);
            LOGGER.debug("Parameter: userId={}", userId);

            int affectedRows = statement.executeUpdate();
            LOGGER.info("Confirm user ID: {} - {} rows affected", userId, affectedRows);

            if (affectedRows > 0) {
                connection.commit();
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.error("SQL Error confirming user with ID: " + userId, e);
            throw new DataException("Failed to confirm user with ID: " + userId, e);
        }
    }
}