package com.example.demo.validator.impl;

import com.example.demo.entity.User;
import com.example.demo.validator.UserValidator;

public class UserValidatorImpl implements UserValidator {

    String LOGIN_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    String PASSWORD_REGEX = "^.{3,50}$";

    @Override
    public boolean isValid(User user) {
        return isValidLogin(user.getLogin()) && isValidPassword(user.getPassword());
    }

    @Override
    public boolean isValidLogin(String login) {
        return login != null && login.matches(LOGIN_REGEX);
    }

    @Override
    public boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }
}