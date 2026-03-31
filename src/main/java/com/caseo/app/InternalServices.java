package com.caseo.app;

import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;
import java.util.HashMap;
import java.util.Map;

public class InternalServices {

    private final Map<Class<?>, ParentService<?>> parentServices = new HashMap<>();
    private final Map<Class<?>, ChildService<?>> childServices = new HashMap<>();

    public InternalServices(RepositoryContext repoContext) {
        // Автоматически создаем ParentService для всех зарегистрированных родителей
        repoContext.getAllParentClasses().forEach(clazz ->
                parentServices.put(clazz, new ParentService<>(repoContext.getParent(clazz))));

        // Автоматически создаем ChildService для всех зарегистрированных детей
        repoContext.getAllChildClasses().forEach(clazz ->
                childServices.put(clazz, new ChildService<>(repoContext.getChild(clazz))));
    }

    @SuppressWarnings("unchecked")
    public <T> ParentService<T> getParentService(Class<T> entityClass) {
        ParentService<T> service = (ParentService<T>) parentServices.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("ParentService не найден для: " + entityClass.getSimpleName());
        }
        return service;
    }

    @SuppressWarnings("unchecked")
    public <T> ChildService<T> getChildService(Class<T> entityClass) {
        ChildService<T> service = (ChildService<T>) childServices.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("ChildService не найден для: " + entityClass.getSimpleName());
        }
        return service;
    }
}
