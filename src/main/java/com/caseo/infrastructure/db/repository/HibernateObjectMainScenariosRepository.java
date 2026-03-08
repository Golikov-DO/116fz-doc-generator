package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectMainScenarios;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectMainScenariosRepository implements ChildRepository<ObjectMainScenarios> {
    @Override
    public ObjectMainScenarios findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectMainScenarios.class, objectId)
        );
    }

    @Override
    public List<ObjectMainScenarios> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectMainScenarios where object.id = :objectId order by id",
                                ObjectMainScenarios.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectMainScenarios objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectMainScenarios mainScenarios = session.get(ObjectMainScenarios.class, id);
            if (mainScenarios != null) {
                session.remove(mainScenarios);
            }
        });
    }
}
