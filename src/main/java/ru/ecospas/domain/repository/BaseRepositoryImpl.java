package ru.ecospas.domain.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;
import ru.ecospas.domain.model.BaseEntity;

import java.util.List;

public class BaseRepositoryImpl<T extends BaseEntity>
        extends SimpleJpaRepository<T, Integer>
        implements BaseRepository<T> {

    private final Class<T> domainClass;
    private final EntityManager entityManager;

    public BaseRepositoryImpl(
            JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager
    ) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }

    @Override
    public List<T> findManyByParentId(int parentId) {

        String parentField = Character.toLowerCase(
                domainClass.getSimpleName().charAt(0)
        ) + domainClass.getSimpleName().substring(1);

        return entityManager.createQuery(
                        "from " + domainClass.getSimpleName()
                                + " e where e." + parentField + ".id = :id",
                        domainClass
                )
                .setParameter("id", parentId)
                .getResultList();
    }

    @Override
    public T findOneByParentId(int parentId) {

        return findManyByParentId(parentId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    @NonNull
    public Class<T> getDomainClass() {
        return domainClass;
    }
}