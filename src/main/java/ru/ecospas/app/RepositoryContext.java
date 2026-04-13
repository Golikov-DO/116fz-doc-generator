package ru.ecospas.app;

import ru.ecospas.domain.repository.ChildRepository;
import ru.ecospas.domain.repository.ParentRepository;

import java.util.Map;
import java.util.Set;

// Holds all registered repositories and provides access by entity class
public record RepositoryContext(
        Map<Class<?>, ParentRepository<?>> parentRepos,
        Map<Class<?>, ChildRepository<?>> childRepos
) {

    @SuppressWarnings("unchecked") // Safe cast: repositories are stored by entity class
    public <T> ParentRepository<T> getParent(Class<T> entityClass) {
        return (ParentRepository<T>) parentRepos.get(entityClass);
    }

    @SuppressWarnings("unchecked") // Safe cast: repositories are stored by entity class
    public <T> ChildRepository<T> getChild(Class<T> entityClass) {
        return (ChildRepository<T>) childRepos.get(entityClass);
    }

    // Returns all registered parent entity classes
    public Set<Class<?>> getAllParentClasses() {
        return parentRepos.keySet();
    }

    // Returns all registered child entity classes
    public Set<Class<?>> getAllChildClasses() {
        return childRepos.keySet();
    }

}