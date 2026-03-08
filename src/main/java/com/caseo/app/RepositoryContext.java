package com.caseo.app;

import com.caseo.domain.repository.*;

import java.util.Map;

public record RepositoryContext(
        Map<Class<?>, ParentRepository<?>> parentRepos,
        Map<Class<?>, ChildRepository<?>> childRepos
) {
    @SuppressWarnings("unchecked")
    public <T> ParentRepository<T> getParent(Class<T> entityClass) {
        return (ParentRepository<T>) parentRepos.get(entityClass);
    }

    @SuppressWarnings("unchecked")
    public <T> ChildRepository<T> getChild(Class<T> entityClass) {
        return (ChildRepository<T>) childRepos.get(entityClass);
    }
}
