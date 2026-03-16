package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfDocumentImageRepository implements ChildRepository<AsfDocumentImage> {
    @Override
    public AsfDocumentImage findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfDocumentImage where asf.id = :asfId", AsfDocumentImage.class)
                        .setParameter("asfId", asfId)
                        .uniqueResult()
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
    public void save(AsfDocumentImage entity) {
        HibernateUtil.inTransaction(session -> {
            if (entity.getId() == null) {
                session.persist(entity);  // persist присваивает ID
            } else {
                session.merge(entity);
            }
        });
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
