package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.exception.DataException;

import java.util.Optional;

public interface UserDao {
    boolean authenticate(String login, String password) throws DataException;
    boolean insert(User user) throws DataException;
    Optional<User> findByLogin(String login) throws DataException;
}
