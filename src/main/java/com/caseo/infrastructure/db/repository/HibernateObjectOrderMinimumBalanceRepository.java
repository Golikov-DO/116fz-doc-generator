package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectOrderMinimumBalance;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectOrderMinimumBalanceRepository implements ChildRepository<ObjectOrderMinimumBalance> {
    @Override
    public ObjectOrderMinimumBalance findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectOrderMinimumBalance.class, objectId)
        );
    }

    @Override
    public List<ObjectOrderMinimumBalance> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectOrderMinimumBalance where object.id = :objectId order by id",
                                ObjectOrderMinimumBalance.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectOrderMinimumBalance objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectOrderMinimumBalance orderMinimumBalance = session.get(ObjectOrderMinimumBalance.class, id);
            if (orderMinimumBalance != null) {
                session.remove(orderMinimumBalance);
            }
        });
    }
}
