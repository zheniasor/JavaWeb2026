package com.example.demo.dao.impl;

import com.example.demo.db.ConnectionPool;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoImplTest {

    private UserDaoImpl userDao;

    @BeforeAll
    void setUpDatabase() throws DataException {
        userDao = new UserDaoImpl();
        cleanDatabase();
    }

    @BeforeEach
    void cleanDatabase() {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM users");
            // Для SQL Server
            stmt.execute("DBCC CHECKIDENT ('users', RESEED, 0)");
        } catch (SQLException | DataException e) {
            // Пропускаем, если таблица пустая
        }
    }

    @Test
    @DisplayName("Должен вставить нового пользователя")
    void insert_ShouldReturnTrue_WhenUserIsValid() throws DataException {
        User user = new User("john123", PasswordEncoder.encode("pass123"), "john@example.com");
        user.setConfirmationToken("token123");
        user.setConfirmed(false);

        boolean result = userDao.insert(user);

        assertThat(result).isTrue();
        assertThat(user.getId()).isPositive();
    }

    @Test
    @DisplayName("Должен найти пользователя по логину")
    void findByLogin_ShouldReturnUser_WhenUserExists() throws DataException {
        User user = new User("john123", PasswordEncoder.encode("pass123"), "john@example.com");
        userDao.insert(user);

        Optional<User> found = userDao.findByLogin("john123");

        assertThat(found).isPresent();
        assertThat(found.get().getLogin()).isEqualTo("john123");
    }

    @Test
    @DisplayName("Должен вернуть пустой Optional при поиске несуществующего логина")
    void findByLogin_ShouldReturnEmpty_WhenUserNotFound() throws DataException {
        Optional<User> found = userDao.findByLogin("nonexistent");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Должен найти всех пользователей")
    void findAll_ShouldReturnListOfUsers() throws DataException {
        userDao.insert(new User("user1", PasswordEncoder.encode("pass1"), "user1@example.com"));
        userDao.insert(new User("user2", PasswordEncoder.encode("pass2"), "user2@example.com"));

        List<User> users = userDao.findAll();

        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }
}