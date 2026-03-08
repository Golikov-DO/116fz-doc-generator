package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectTechnologicalBlock;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectTechnologicalBlockRepository implements ChildRepository<ObjectTechnologicalBlock> {
    @Override
    public ObjectTechnologicalBlock findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectTechnologicalBlock.class, objectId)
        );
    }

    @Override
    public List<ObjectTechnologicalBlock> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectTechnologicalBlock where object.id = :objectId order by num",
                                ObjectTechnologicalBlock.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectTechnologicalBlock objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectTechnologicalBlock technologicalBlock = session.get(ObjectTechnologicalBlock.class, id);
            if (technologicalBlock != null) {
                session.remove(technologicalBlock);
            }
        });
    }
}
