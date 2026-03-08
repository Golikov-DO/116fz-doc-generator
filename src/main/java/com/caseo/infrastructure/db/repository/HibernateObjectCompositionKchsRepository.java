package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectCompositionKchs;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectCompositionKchsRepository implements ChildRepository<ObjectCompositionKchs> {
    @Override
    public ObjectCompositionKchs findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectCompositionKchs.class, objectId)
        );
    }

    @Override
    public List<ObjectCompositionKchs> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectCompositionKchs where object.id = :objectId order by id",
                                ObjectCompositionKchs.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectCompositionKchs kchs) {
        HibernateUtil.inTransaction(session -> {
            if (kchs.getId() == null) {  // Новый объект
                session.persist(kchs);
            } else {  // Существующий объект
                session.merge(kchs);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectCompositionKchs kchs = session.get(ObjectCompositionKchs.class, id);
            if (kchs != null) {
                session.remove(kchs);
            }
        });
    }
}
