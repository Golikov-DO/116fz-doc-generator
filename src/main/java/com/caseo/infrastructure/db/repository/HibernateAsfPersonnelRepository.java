package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfPersonnel;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfPersonnelRepository implements ChildRepository<AsfPersonnel> {
    @Override
    public AsfPersonnel findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery(
                                "from AsfPersonnel where asf.id = :asfId",
                                AsfPersonnel.class
                        )
                        .setParameter("asfId", asfId)
                        .uniqueResult()
        );
    }

    @Override
    public List<AsfPersonnel> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfPersonnel where asf.id = :asfId order by id",
                                AsfPersonnel.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfPersonnel asfPersonnel) {
        HibernateUtil.inTransaction(session -> session.merge(asfPersonnel));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfPersonnel asfPersonnel = session.get(AsfPersonnel.class, id);
            if (asfPersonnel != null) {
                session.remove(asfPersonnel);
            }
        });
    }
}
