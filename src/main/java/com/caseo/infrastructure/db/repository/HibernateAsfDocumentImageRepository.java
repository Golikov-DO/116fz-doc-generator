package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfDocumentImageRepository implements ChildRepository<AsfDocumentImage> {
    @Override
    public AsfDocumentImage findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.get(AsfDocumentImage.class, asfId)
        );
    }

    @Override
    public List<AsfDocumentImage> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfDocumentImage where asf.id = :asfId order by groupKey, id",
                                AsfDocumentImage.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfDocumentImage asfDocumentImage) {
        HibernateUtil.inTransaction(session -> session.merge(asfDocumentImage));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfDocumentImage asfDocumentImage = session.get(AsfDocumentImage.class, id);
            if (asfDocumentImage != null) {
                session.remove(asfDocumentImage);
            }
        });
    }
}
