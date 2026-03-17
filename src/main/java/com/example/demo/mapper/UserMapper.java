package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.constants.ColumnName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    private UserMapper() {}

    public static User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(ColumnName.ID));
        user.setLogin(resultSet.getString(ColumnName.LOGIN));
        user.setPassword(resultSet.getString(ColumnName.PASSWORD));
        return user;
    }
}