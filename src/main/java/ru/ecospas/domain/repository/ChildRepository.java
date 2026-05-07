package ru.ecospas.domain.repository;

import java.util.List;

public interface ChildRepository<T> extends CrudRepository<T> {
    T findOneByParentId(int parentId);
    List<T> findManyByParentId(int parentId);
}
