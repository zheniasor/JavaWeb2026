package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dao.impl.UserDaoImpl;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.MailService;
import com.example.demo.service.UserService;
import com.example.demo.util.PasswordEncoder;
import com.example.demo.validator.UserValidator;
import com.example.demo.validator.impl.UserValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl instance = new UserServiceImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final UserValidator userValidator = new UserValidatorImpl();
    private final MailService mailService = MailServiceImpl.getInstance(); // ← интерфейс

    private UserServiceImpl() {
        LOGGER.debug("UserServiceImpl instance created");
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String login, String password) throws DataException {
        LOGGER.debug("Authenticating user: {}", login);
        Optional<User> userOpt = userDao.findByLogin(login);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        if (!user.isConfirmed()) {
            LOGGER.warn("User {} not confirmed", login);
            return false;
        }

        return PasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean register(User user) throws DataException {
        LOGGER.debug("Attempting to register user: {}", user.getLogin());

        Map<String, String> errors = userValidator.validate(user);
        if (!errors.isEmpty()) {
            LOGGER.warn("Registration failed - validation error for user: {}", user.getLogin());
            return false;
        }

        Optional<User> existingUser = userDao.findByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            LOGGER.warn("Registration failed - user already exists: {}", user.getLogin());
            return false;
        }

        Optional<User> existingEmail = userDao.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            LOGGER.warn("Registration failed - email already exists: {}", user.getEmail());
            return false;
        }

        String confirmationToken = java.util.UUID.randomUUID().toString();
        user.setConfirmationToken(confirmationToken);
        user.setConfirmed(false);

        String hashedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        boolean result = userDao.insert(user);

        if (result) {
            // Используем интерфейс MailService
            mailService.sendConfirmationEmail(user);
            LOGGER.info("User registered successfully, confirmation email sent: {}", user.getLogin());
        } else {
            LOGGER.error("Failed to register user: {}", user.getLogin());
        }

        return result;
    }

    @Override
    public Optional<User> findByToken(String token) throws DataException {
        LOGGER.debug("Finding user by token");
        return userDao.findByToken(token);
    }

    @Override
    public boolean confirmUser(int userId) throws DataException {
        LOGGER.debug("Confirming user: {}", userId);
        return userDao.confirmUser(userId);
    }
}