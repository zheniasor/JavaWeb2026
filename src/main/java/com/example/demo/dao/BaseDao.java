package com.example.demo.dao;

import com.example.demo.entity.AbstractEntity;
import com.example.demo.exception.DataException;

import java.util.List;

public abstract class BaseDao<T extends AbstractEntity> {
    public abstract boolean insert(T t) throws DataException;
    public abstract boolean delete(T t) throws DataException;
    public abstract List<T> findAll() throws DataException;
    public abstract T update(T t) throws DataException;
}
