package ru.ecospas.domain.service;

import ru.ecospas.domain.model.BaseEntity;
import ru.ecospas.domain.repository.BaseRepository;

import java.util.List;

public class ChildService<T extends BaseEntity> implements ChildOperations<T> {

    protected final BaseRepository<T> repository;

    public ChildService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> getManyByParentId(int parentId) {
        return null;
    }

    @Override
    public T getOneByParentId(int parentId) {
        return null;
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