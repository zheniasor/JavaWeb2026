package com.example.demo.validator;

import com.example.demo.entity.User;

import java.util.Map;

public interface UserValidator {

    boolean isValid(User user);

    Map<String, String> validate(User user);
}