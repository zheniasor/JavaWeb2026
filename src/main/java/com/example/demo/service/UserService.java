package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DataException;

import java.util.List;
import java.util.Optional;

public interface UserService {
     boolean authenticate(String login, String password) throws DataException;
     boolean register(User user) throws DataException;
     Optional<User> findByToken(String token) throws DataException;
     boolean confirmUser(int userId) throws DataException;
     User update(User user) throws DataException;
     List<User> findAll() throws DataException;
     boolean deleteUser(int userId) throws DataException;
     Optional<User> findByLogin(String login) throws DataException;
     Optional<User> findByEmail(String email) throws DataException;
     boolean updateAvatar(int userId, String avatarPath) throws DataException;
     }
