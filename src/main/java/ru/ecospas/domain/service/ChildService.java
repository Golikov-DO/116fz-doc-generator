package ru.ecospas.domain.service;

import ru.ecospas.domain.repository.ChildRepository;

import java.util.List;

public class ChildService<T> implements ChildOperations<T> {

    protected final ChildRepository<T> repository;

    public ChildService(ChildRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> getManyByParentId(int parentId) {
        return repository.findManyByParentId(parentId);
    }

    @Override
    public T getOneByParentId(int parentId) {
        return repository.findOneByParentId(parentId);
    }

    @Override
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }
}