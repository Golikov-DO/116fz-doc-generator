package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfWorkTypeRepository implements ChildRepository<AsfWorkType> {
    @Override
    public AsfWorkType findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.get(AsfWorkType.class, asfId)
        );
    }

    @Override
    public List<AsfWorkType> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfWorkType where asf.id = :asfId order by id",
                                AsfWorkType.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfWorkType asfWorkType) {
        HibernateUtil.inTransaction(session -> session.merge(asfWorkType));

    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfWorkType asfWorkType = session.get(AsfWorkType.class, id);
            if (asfWorkType != null) {
                session.remove(asfWorkType);
            }
        });
    }
}
