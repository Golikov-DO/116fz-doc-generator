package com.caseo.domain.repository;

import java.util.List;

public interface ChildRepository<T> {
    T findOneByParentId(int parentId);
    List<T> findManyByParentId(int parentId);
    void save(T entity);
    void deleteById(int id);
}
