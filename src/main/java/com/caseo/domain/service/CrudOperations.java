package com.caseo.domain.service;

public interface CrudOperations<T> {
    void save(T entity);
    void deleteById(int id);
}
