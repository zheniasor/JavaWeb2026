package com.example.demo.mapper;

import com.example.demo.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserMapper {

    User mapRow(ResultSet resultSet) throws SQLException;
}