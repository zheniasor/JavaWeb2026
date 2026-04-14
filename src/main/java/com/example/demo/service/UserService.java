package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DataException;

import java.util.Optional;

public interface UserService {
     boolean authenticate(String login, String password) throws DataException;
     boolean register(User user) throws DataException;
     Optional<User> findByToken(String token) throws DataException;
     boolean confirmUser(int userId) throws DataException;
}
