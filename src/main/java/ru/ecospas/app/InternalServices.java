package ru.ecospas.app;

import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.HashMap;
import java.util.Map;

public class InternalServices {

    // Stores services for parent entities (no parent reference required)
    private final Map<Class<?>, ParentService<?>> parentServices = new HashMap<>();

    // Stores services for child entities (linked to a parent)
    private final Map<Class<?>, ChildService<?>> childServices = new HashMap<>();

    public InternalServices(RepositoryContext repoContext) {
        // Automatically create ParentService for all registered parent entities
        repoContext.getAllParentClasses().forEach(clazz ->
                parentServices.put(clazz, new ParentService<>(repoContext.getParent(clazz))));

        // Automatically create ChildService for all registered child entities
        repoContext.getAllChildClasses().forEach(clazz ->
                childServices.put(clazz, new ChildService<>(repoContext.getChild(clazz))));
    }

    @SuppressWarnings("unchecked") // Safe cast: services are registered by entity class
    public <T> ParentService<T> getParentService(Class<T> entityClass) {
        ParentService<T> service = (ParentService<T>) parentServices.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("ParentService не найден для: " + entityClass.getSimpleName());
        }
        return service;
    }

    @SuppressWarnings("unchecked") // Safe cast: services are registered by entity class
    public <T> ChildService<T> getChildService(Class<T> entityClass) {
        ChildService<T> service = (ChildService<T>) childServices.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("ChildService не найден для: " + entityClass.getSimpleName());
        }
        return service;
    }
}