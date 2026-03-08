package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectAddressRepository implements ChildRepository<ObjectAddress> {
    @Override
    public ObjectAddress findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectAddress where object.id = :objectId",
                                ObjectAddress.class)
                        .setParameter("objectId", objectId)
                        .uniqueResultOptional()
                        .orElse(null)
        );
    }

    @Override
    public List<ObjectAddress> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectAddress where object.id = :objectId order by id",
                                ObjectAddress.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectAddress objectAddress) {
        HibernateUtil.inTransaction(session -> {
            if (objectAddress.getId() == null) {  // Новый объект
                session.persist(objectAddress);
            } else {  // Существующий объект
                session.merge(objectAddress);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectAddress objectAddress = session.get(ObjectAddress.class, id);
            if (objectAddress != null) {
                session.remove(objectAddress);
            }
        });
    }
}
