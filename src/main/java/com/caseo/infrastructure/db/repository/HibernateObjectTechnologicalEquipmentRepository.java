package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectTechnologicalEquipment;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectTechnologicalEquipmentRepository implements ChildRepository<ObjectTechnologicalEquipment> {
    @Override
    public ObjectTechnologicalEquipment findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectTechnologicalEquipment.class, objectId)
        );
    }

    @Override
    public List<ObjectTechnologicalEquipment> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectTechnologicalEquipment where object.id = :objectId order by num",
                                ObjectTechnologicalEquipment.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectTechnologicalEquipment objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectTechnologicalEquipment technologicalEquipment = session.get(ObjectTechnologicalEquipment.class, id);
            if (technologicalEquipment != null) {
                session.remove(technologicalEquipment);
            }
        });
    }
}
