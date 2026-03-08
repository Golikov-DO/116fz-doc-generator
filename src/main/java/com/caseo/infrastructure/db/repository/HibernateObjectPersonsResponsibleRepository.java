package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectPersonsResponsible;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectPersonsResponsibleRepository implements ChildRepository<ObjectPersonsResponsible> {
    @Override
    public ObjectPersonsResponsible findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectPersonsResponsible.class, objectId)
        );
    }

    @Override
    public List<ObjectPersonsResponsible> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectPersonsResponsible where object.id = :objectId order by id",
                                ObjectPersonsResponsible.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectPersonsResponsible objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectPersonsResponsible personsResponsible = session.get(ObjectPersonsResponsible.class, id);
            if (personsResponsible != null) {
                session.remove(personsResponsible);
            }
        });
    }
}
