package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectType;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectTypeRepository implements ChildRepository<ObjectType> {
    @Override
    public ObjectType findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectType.class, objectId)
        );
    }

    @Override
    public List<ObjectType> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectType where object.id = :objectId order by id",
                                ObjectType.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectType objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectType objectType = session.get(ObjectType.class, id);
            if (objectType != null) {
                session.remove(objectType);
            }
        });
    }
}
