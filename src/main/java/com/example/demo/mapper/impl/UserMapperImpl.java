package com.example.demo.mapper.impl;

import com.example.demo.entity.User;
import com.example.demo.dao.ColumnName;
import com.example.demo.mapper.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapperImpl implements UserMapper {

    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(ColumnName.ID));
        user.setLogin(resultSet.getString(ColumnName.LOGIN));
        user.setPassword(resultSet.getString(ColumnName.PASSWORD));
        return user;
    }
}