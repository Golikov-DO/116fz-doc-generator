package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectFireEquipment;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectFireEquipmentRepository implements ChildRepository<ObjectFireEquipment> {
    @Override
    public ObjectFireEquipment findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectFireEquipment.class, objectId)
        );
    }

    @Override
    public List<ObjectFireEquipment> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectFireEquipment where object.id = :objectId order by id",
                                ObjectFireEquipment.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectFireEquipment objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectFireEquipment fireEquipment = session.get(ObjectFireEquipment.class, id);
            if (fireEquipment != null) {
                session.remove(fireEquipment);
            }
        });
    }
}
