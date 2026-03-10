package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dao.impl.UserDaoImpl;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl instance = new UserServiceImpl();
    private final UserDao userDao = new UserDaoImpl();

    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 3;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final String ERROR_LOGIN_EMPTY = "Login cannot be empty";
    private static final String ERROR_PASSWORD_EMPTY = "Password cannot be empty";
    private static final String ERROR_LOGIN_TOO_SHORT = "Login must be at least " + MIN_LOGIN_LENGTH + " characters";
    private static final String ERROR_LOGIN_TOO_LONG = "Login must not exceed " + MAX_LOGIN_LENGTH + " characters";
    private static final String ERROR_PASSWORD_TOO_SHORT = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    private static final String ERROR_PASSWORD_TOO_LONG = "Password must not exceed " + MAX_PASSWORD_LENGTH + " characters";
    private static final String ERROR_INVALID_CHARACTERS = "Login contains invalid characters";
    private static final String LOGIN_PATTERN = "^[a-zA-Z0-9_]+$";

    private UserServiceImpl() {
        LOGGER.debug("UserServiceImpl instance created");
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String login, String password) throws DataException {
        LOGGER.debug("Authenticating user: {}", login);
        return userDao.authenticate(login, password);
    }

    @Override
    public boolean register(User user) throws DataException {
        LOGGER.debug("Attempting to register user: {}", user.getLogin());

        String validationError = validateUser(user);
        if (validationError != null) {
            LOGGER.warn("Registration failed - validation error: {}", validationError);
            return false;
        }

        Optional<User> existingUser = userDao.findByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            LOGGER.warn("Registration failed - user already exists: {}", user.getLogin());
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        boolean result = userDao.insert(user);

        if (result) {
            LOGGER.info("User registered successfully: {}", user.getLogin());
        } else {
            LOGGER.error("Failed to register user: {}", user.getLogin());
        }

        return result;
    }

    private String validateUser(User user) {
        String login = user.getLogin();
        String password = user.getPassword();

        if (login == null || login.trim().isEmpty()) {
            return ERROR_LOGIN_EMPTY;
        }
        if (password == null || password.trim().isEmpty()) {
            return ERROR_PASSWORD_EMPTY;
        }

        login = login.trim();
        if (login.length() < MIN_LOGIN_LENGTH) {
            return ERROR_LOGIN_TOO_SHORT;
        }
        if (login.length() > MAX_LOGIN_LENGTH) {
            return ERROR_LOGIN_TOO_LONG;
        }

        if (!login.matches(LOGIN_PATTERN)) {
            return ERROR_INVALID_CHARACTERS;
        }

        password = password.trim();
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return ERROR_PASSWORD_TOO_SHORT;
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return ERROR_PASSWORD_TOO_LONG;
        }

        return null;
    }
}