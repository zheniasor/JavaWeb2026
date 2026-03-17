package com.example.demo.validator;

import com.example.demo.entity.User;

public class UserValidator {

    private static final String LOGIN_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
    private static final String PASSWORD_PATTERN = "^.{3,50}$";

    private UserValidator() {}

    public static boolean isValid(User user) {
        return isValidLogin(user.getLogin()) && isValidPassword(user.getPassword());
    }

    public static boolean isValidLogin(String login) {
        return login != null && login.matches(LOGIN_PATTERN);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}