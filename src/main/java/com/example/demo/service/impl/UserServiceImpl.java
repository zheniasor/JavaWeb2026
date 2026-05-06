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

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl instance = new UserServiceImpl();
    private  UserDao userDao = new UserDaoImpl();
    private  UserValidator userValidator = new UserValidatorImpl();
    private  MailService mailService = MailServiceImpl.getInstance();

    public UserServiceImpl(UserDao userDao, UserValidator userValidator, MailService mailService) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.mailService = mailService;
    }

    private UserServiceImpl() {
        this.userDao = new UserDaoImpl();
        this.userValidator = new UserValidatorImpl();
        this.mailService = MailServiceImpl.getInstance();
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

    @Override
    public User update(User user) throws DataException {
        LOGGER.debug("Updating user: {}", user.getLogin());

        Map<String, String> errors = userValidator.validate(user);
        if (!errors.isEmpty()) {
            LOGGER.warn("Update failed - validation error for user: {}", user.getLogin());
            return null;
        }

       String hashedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userDao.update(user);
    }

    @Override
    public List<User> findAll() throws DataException {
        LOGGER.debug("Fetching all users");
        return userDao.findAll();
    }

    @Override
    public boolean deleteUser(int userId) throws DataException {
        LOGGER.debug("Deleting user with ID: {}", userId);
        return userDao.deleteUser(userId);
    }

    @Override
    public Optional<User> findByLogin(String login) throws DataException {
        LOGGER.debug("Finding user by login: {}", login);
        return userDao.findByLogin(login);
    }

    @Override
    public Optional<User> findByEmail(String email) throws DataException {
        LOGGER.debug("Finding user by email: {}", email);
        return userDao.findByEmail(email);
    }

    @Override
    public boolean updateAvatar(int userId, String avatarPath) throws DataException {
        LOGGER.debug("Updating avatar for user ID: {}", userId);
        return userDao.updateAvatar(userId, avatarPath);
    }
}