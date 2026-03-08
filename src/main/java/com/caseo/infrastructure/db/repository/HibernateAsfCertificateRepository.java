package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfCertificate;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfCertificateRepository implements ChildRepository<AsfCertificate> {
    @Override
    public AsfCertificate findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.get(AsfCertificate.class, asfId)
        );
    }

    @Override
    public List<AsfCertificate> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfCertificate where asf.id = :asfId order by id",
                                AsfCertificate.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfCertificate asfCertificate) {
        HibernateUtil.inTransaction(session -> session.merge(asfCertificate));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfCertificate asfCertificate = session.get(AsfCertificate.class, id);
            if (asfCertificate != null) {
                session.remove(asfCertificate);
            }
        });
    }
}
