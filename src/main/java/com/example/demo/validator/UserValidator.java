package com.example.demo.validator;

import com.example.demo.entity.User;

public interface UserValidator {

    boolean isValid(User user);

    boolean isValidLogin(String login);

    boolean isValidPassword(String password);
}