package ru.ecospas.infrastructure.db.repository;

import ru.ecospas.domain.model.BaseEntity;
import ru.ecospas.domain.repository.ChildRepository;
import ru.ecospas.domain.repository.ParentRepository;
import ru.ecospas.infrastructure.db.HibernateUtil;
import java.util.List;

public class GenericHibernateRepository<T extends BaseEntity> 
        implements ParentRepository<T>, ChildRepository<T> {

    private final Class<T> entityClass;
    private final String parentFieldName;
    private final String orderBy;

    public GenericHibernateRepository(Class<T> entityClass, String parentFieldName, String orderBy) {
        this.entityClass = entityClass;
        this.parentFieldName = parentFieldName;
        this.orderBy = orderBy;
    }

    @Override
    public T findOneById(int id) {
        return HibernateUtil.inSession(session -> session.get(entityClass, id));
    }

    @Override
    public List<T> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from " + entityClass.getSimpleName() + " order by " + orderBy, entityClass).list());
    }

    @Override
    public T findOneByParentId(int parentId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from " + entityClass.getSimpleName() + 
                        " where " + parentFieldName + ".id = :pId", entityClass)
                        .setParameter("pId", parentId)
                        .uniqueResult());
    }

    @Override
    public List<T> findManyByParentId(int parentId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from " + entityClass.getSimpleName() + 
                        " where " + parentFieldName + ".id = :pId order by " + orderBy, entityClass)
                        .setParameter("pId", parentId).list());
    }

    @Override
    public void save(T entity) {
        HibernateUtil.inTransaction(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                session.merge(entity);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            T entity = session.get(entityClass, id);
            if (entity != null) session.remove(entity);
        });
    }
}
