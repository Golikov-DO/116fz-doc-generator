package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousParam;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectHazardousParamRepository implements ChildRepository<ObjectHazardousParam>, ParentRepository<ObjectHazardousParam> {

    @Override
    public ObjectHazardousParam findOneByParentId(int substanceId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectHazardousParam.class, substanceId)
        );
    }

    @Override
    public List<ObjectHazardousParam> findManyByParentId(int substanceId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectHazardousParam where substance.id = :substanceId",
                                ObjectHazardousParam.class)
                        .setParameter("substanceId", substanceId)
                        .list()
        );
    }

    @Override
    public ObjectHazardousParam findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectHazardousParam.class, id)
        );
    }

    @Override
    public List<ObjectHazardousParam> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectHazardousParam order by id", ObjectHazardousParam.class)
                        .list()
        );
    }

    @Override
    public void save(ObjectHazardousParam hazardousParam) {
        HibernateUtil.inTransaction(session -> session.merge(hazardousParam));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectHazardousParam param = session.get(ObjectHazardousParam.class, id);
            if (param != null) {
                session.remove(param);
            }
        });
    }
}