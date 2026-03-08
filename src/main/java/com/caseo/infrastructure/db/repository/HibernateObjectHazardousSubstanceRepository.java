package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousSubstance;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectHazardousSubstanceRepository implements ParentRepository<ObjectHazardousSubstance> {

    @Override
    public ObjectHazardousSubstance findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectHazardousSubstance.class, id)
        );
    }

    @Override
    public List<ObjectHazardousSubstance> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectHazardousSubstance order by name",
                                ObjectHazardousSubstance.class)
                        .list()
        );
    }

    @Override
    public void save(ObjectHazardousSubstance substance) {
        HibernateUtil.inTransaction(session -> session.merge(substance));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectHazardousSubstance substance = session.get(ObjectHazardousSubstance.class, id);
            if (substance != null) {
                session.remove(substance);
            }
        });
    }
}