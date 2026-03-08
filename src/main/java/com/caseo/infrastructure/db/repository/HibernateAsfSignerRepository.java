package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfSignerRepository implements ChildRepository<AsfSigner> {
    @Override
    public AsfSigner findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.get(AsfSigner.class, asfId)
        );
    }

    @Override
    public List<AsfSigner> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfSigner where asf.id = :asfId order by name",
                                AsfSigner.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfSigner asfId) {
        HibernateUtil.inTransaction(session -> session.merge(asfId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfSigner asfSigner = session.get(AsfSigner.class, id);
            if (asfSigner != null) {
                session.remove(asfSigner);
            }
        });
    }
}
