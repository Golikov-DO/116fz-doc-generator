package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectImage;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectImageRepository implements ChildRepository<ObjectImage> {
    @Override
    public ObjectImage findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectImage.class, objectId)
        );
    }

    @Override
    public List<ObjectImage> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectImage where object.id = :objectId order by id",
                                ObjectImage.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectImage objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectImage objectImage = session.get(ObjectImage.class, id);
            if (objectImage != null) {
                session.remove(objectImage);
            }
        });
    }
}
