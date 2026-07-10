package ru.ecospas.app;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.BaseEntity;
import ru.ecospas.domain.repository.BaseRepository;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InternalServices {

    private final Map<Class<?>, ParentService<?>> parentServices = new HashMap<>();

    private final Map<Class<?>, ChildService<?>> childServices = new HashMap<>();

    public InternalServices(List<BaseRepository<?>> repositories) {
        repositories.forEach(this::register);
    }

    private <T extends BaseEntity> void register(BaseRepository<T> repository) {

        Class<T> entityClass = repository.getDomainClass();

        parentServices.put(
                entityClass,
                new ParentService<>(repository)
        );

        childServices.put(
                entityClass,
                new ChildService<>(repository)
        );
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> ParentService<T> getParentService(Class<T> entityClass) {
        ParentService<T> service = (ParentService<T>) parentServices.get(entityClass);

        if (service == null) {
            throw new IllegalArgumentException(
                    "ParentService не найден для: " + entityClass.getSimpleName()
            );
        }

        return service;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> ChildService<T> getChildService(Class<T> entityClass) {
        ChildService<T> service = (ChildService<T>) childServices.get(entityClass);

        if (service == null) {
            throw new IllegalArgumentException(
                    "ChildService не найден для: " + entityClass.getSimpleName()
            );
        }

        return service;
    }
}