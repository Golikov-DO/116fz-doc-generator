package com.caseo.domain.repository;

import java.util.List;

public interface ParentRepository<T> {
    T findOneById(int id);
    List<T> findMany();
    void save(T entity);
    void deleteById(int id);
}
