package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAccidentScenarios;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectAccidentScenariosRepository implements ChildRepository<ObjectAccidentScenarios> {
    @Override
    public ObjectAccidentScenarios findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectAccidentScenarios.class, objectId)
        );
    }

    @Override
    public List<ObjectAccidentScenarios> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectAccidentScenarios where object.id = :objectId order by id",
                                ObjectAccidentScenarios.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectAccidentScenarios objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectAccidentScenarios accidentScenarios = session.get(ObjectAccidentScenarios.class, id);
            if (accidentScenarios != null) {
                session.remove(accidentScenarios);
            }
        });
    }
}
