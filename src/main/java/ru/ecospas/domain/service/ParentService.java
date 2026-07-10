package ru.ecospas.domain.service;

import ru.ecospas.domain.model.BaseEntity;
import ru.ecospas.domain.repository.BaseRepository;

import java.util.List;

public class ParentService<T extends BaseEntity> implements ParentOperations<T> {

    protected final BaseRepository<T> repository;

    public ParentService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public T getOneById(int id) {
        return repository.findById(id)
                .orElse(null);
    }

    public List<T> getMany() {
        return repository.findAll();
    }

    public void save(T entity) {
        repository.save(entity);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}