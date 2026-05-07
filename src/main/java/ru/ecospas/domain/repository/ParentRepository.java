package ru.ecospas.domain.repository;

import java.util.List;

public interface ParentRepository<T> extends CrudRepository<T> {
    T findOneById(int id);
    List<T> findMany();
}
