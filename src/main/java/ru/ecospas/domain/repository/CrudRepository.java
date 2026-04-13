package ru.ecospas.domain.repository;

public interface CrudRepository<T> {
    void save(T entity);
    void deleteById(int id);
}