package com.caseo.domain.service;

import com.caseo.domain.repository.ParentRepository;
import java.util.List;

public class ParentService<T> implements ParentOperations<T> {

    protected final ParentRepository<T> repository;  // private -> protected, и переименовал в repository

    public ParentService(ParentRepository<T> repository) {
        this.repository = repository;
    }

    public T getOneById(int id) {
        return repository.findOneById(id);
    }

    public List<T> getMany() {
        return repository.findMany();
    }

    public void save(T entity) {
        repository.save(entity);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}