package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectHazardousParamValueRepository implements ChildRepository<ObjectHazardousParamValue> {

    @Override
    public ObjectHazardousParamValue findOneByParentId(int paramId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectHazardousParamValue.class, paramId)
        );
    }

    @Override
    public List<ObjectHazardousParamValue> findManyByParentId(int paramId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectHazardousParamValue where param.id = :paramId",
                                ObjectHazardousParamValue.class)
                        .setParameter("paramId", paramId)
                        .list()
        );
    }

    @Override
    public void save(ObjectHazardousParamValue hazardousParamValue) {
        HibernateUtil.inTransaction(session -> session.merge(hazardousParamValue));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectHazardousParamValue hazardousParamValue = session.get(ObjectHazardousParamValue.class, id);
            if (hazardousParamValue != null) {
                session.remove(hazardousParamValue);
            }
        });
    }
}
