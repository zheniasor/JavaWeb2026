package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dao.impl.UserDaoImpl;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.util.PasswordEncoder;
import com.example.demo.validator.UserValidator;
import com.example.demo.validator.impl.UserValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl instance = new UserServiceImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final UserValidator userValidator = new UserValidatorImpl(); // ← добавили

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

       if (!userValidator.isValid(user)) {
            LOGGER.warn("Registration failed - validation error for user: {}", user.getLogin());
            return false;
        }

        Optional<User> existingUser = userDao.findByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            LOGGER.warn("Registration failed - user already exists: {}", user.getLogin());
            return false;
        }

        String hashedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        boolean result = userDao.insert(user);

        LOGGER.info("User registration {} for user: {}", result ? "successful" : "failed", user.getLogin());

        return result;
    }
}