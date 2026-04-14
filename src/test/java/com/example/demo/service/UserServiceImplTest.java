package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.util.PasswordEncoder;
import com.example.demo.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

   @Mock
    private UserDao userDao;

    @Mock
    private UserValidator userValidator;

    @Mock
    private MailService mailService;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao, userValidator,mailService);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void register_ShouldReturnTrue_WhenUserIsValid() throws DataException {
        User user = new User("john123", "password123", "john@example.com");
        when(userValidator.validate(user)).thenReturn(Map.of()); // Нет ошибок валидации
        when(userDao.findByLogin(user.getLogin())).thenReturn(Optional.empty()); // Логин свободен
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty()); // Email свободен
        when(userDao.insert(any(User.class))).thenReturn(true); // Вставка успешна

        boolean result = userService.register(user);

        assertThat(result).isTrue();
        verify(userDao, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("Регистрация не удалась из-за ошибки валидации")
    void register_ShouldReturnFalse_WhenValidationFails() throws DataException {
        User user = new User("ab", "pass", "invalid");
        when(userValidator.validate(user)).thenReturn(Map.of("login", "Login too short"));

        boolean result = userService.register(user);

        assertThat(result).isFalse();
        verify(userDao, never()).insert(any(User.class));
    }

    @Test
    @DisplayName("Регистрация не удалась, так как логин уже существует")
    void register_ShouldReturnFalse_WhenUserAlreadyExists() throws DataException {
        User user = new User("existingUser", "password123", "john@example.com");
        User existingUser = new User("existingUser", "hashedPass", "old@email.com");

        when(userValidator.validate(user)).thenReturn(Map.of());
        when(userDao.findByLogin(user.getLogin())).thenReturn(Optional.of(existingUser));

        boolean result = userService.register(user);

        assertThat(result).isFalse();
        verify(userDao, never()).insert(any(User.class));
    }

    @Test
    @DisplayName("Успешная аутентификация с правильными данными")
    void authenticate_ShouldReturnTrue_WhenCredentialsAreCorrect() throws DataException {
        String login = "john123";
        String password = "password123";
        String hashedPassword = PasswordEncoder.encode(password);

        User user = new User(login, hashedPassword, "john@example.com");
        user.setConfirmed(true);

        when(userDao.findByLogin(login)).thenReturn(Optional.of(user));

        boolean result = userService.authenticate(login, password);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Аутентификация не удалась, так как пользователь не подтверждён")
    void authenticate_ShouldReturnFalse_WhenUserNotConfirmed() throws DataException {
        String login = "john123";
        String password = "password123";
        String hashedPassword = PasswordEncoder.encode(password);

        User user = new User(login, hashedPassword, "john@example.com");
        user.setConfirmed(false);

        when(userDao.findByLogin(login)).thenReturn(Optional.of(user));

        boolean result = userService.authenticate(login, password);

        assertThat(result).isFalse();
    }
}