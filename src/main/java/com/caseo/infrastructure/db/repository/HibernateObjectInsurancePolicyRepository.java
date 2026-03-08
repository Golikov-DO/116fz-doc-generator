package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectInsurancePolicy;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectInsurancePolicyRepository implements ChildRepository<ObjectInsurancePolicy> {
    @Override
    public ObjectInsurancePolicy findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectInsurancePolicy.class, objectId)
        );
    }

    @Override
    public List<ObjectInsurancePolicy> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectInsurancePolicy where object.id = :objectId order by id",
                                ObjectInsurancePolicy.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectInsurancePolicy objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectInsurancePolicy insurancePolicy = session.get(ObjectInsurancePolicy.class, id);
            if (insurancePolicy != null) {
                session.remove(insurancePolicy);
            }
        });
    }
}
