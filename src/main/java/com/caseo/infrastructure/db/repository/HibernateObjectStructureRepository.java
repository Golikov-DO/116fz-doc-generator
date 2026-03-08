package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectStructure;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectStructureRepository implements ChildRepository<ObjectStructure> {
    @Override
    public ObjectStructure findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectStructure.class, objectId)
        );
    }

    @Override
    public List<ObjectStructure> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectStructure where object.id = :objectId order by id",
                                ObjectStructure.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectStructure objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectStructure objectStructure = session.get(ObjectStructure.class, id);
            if (objectStructure != null) {
                session.remove(objectStructure);
            }
        });
    }
}
