package ru.ecospas.domain.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public abstract class AbstractCrudService<E, REQUEST> {

    protected abstract JpaRepository<E, Integer> repository();

    protected abstract E newEntity();

    protected abstract void map(REQUEST request, E entity);

    public List<E> findAll() {
        return repository().findAll();
    }

    public E load(Integer id) {

        return repository()
                .findById(id)
                .orElse(null);
    }

    public E loadRest(Integer id) {

        return load(id);
    }

    @Transactional
    public E create(REQUEST request) {
        return save(request, newEntity());
    }

    @Transactional
    protected E save(REQUEST request, E entity) {
        map(request, entity);
        return repository().save(entity);
    }

    @Transactional
    public E update(Integer id, REQUEST request) {
        E entity = loadRest(id);
        if (entity == null) {
            return null;
        }
        return save(request, entity);
    }

    @Transactional
    public void deleteRest(Integer id) {
        E entity = loadRest(id);
        if (entity == null) {
            return;
        }
        repository().delete(entity);
    }
}