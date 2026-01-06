package com.airline.core.dao;

import java.util.List;

public interface GenericDAO<T extends CSVConvertible, ID> {
    void save(T entity);
    List<T> getAll();
    void update(T entity);
    void delete(ID id);
    T findById(ID id);
}


//ilerisi icin
