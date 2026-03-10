package com.example.demo.util;

import org.mindrot.jbcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordUtil {

    private static final Logger LOGGER = LogManager.getLogger(PasswordUtil.class);

    public static String hashPassword(String plainPassword) {
        LOGGER.debug("Hashing password");
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        LOGGER.debug("Password hashed successfully");
        return hashed;
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        LOGGER.debug("Checking password");
        boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
        LOGGER.debug("Password match: {}", matches);
        return matches;
    }
}