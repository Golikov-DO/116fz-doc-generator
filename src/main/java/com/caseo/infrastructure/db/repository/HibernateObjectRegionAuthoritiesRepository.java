package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectRegionalAuthorities;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectRegionAuthoritiesRepository implements ChildRepository<ObjectRegionalAuthorities> {
    @Override
    public ObjectRegionalAuthorities findOneByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectRegionalAuthorities.class, objectId)
        );
    }

    @Override
    public List<ObjectRegionalAuthorities> findManyByParentId(int objectId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectRegionalAuthorities where object.id = :objectId order by id",
                                ObjectRegionalAuthorities.class)
                        .setParameter("objectId", objectId)
                        .list()
        );
    }

    @Override
    public void save(ObjectRegionalAuthorities objectId) {
        HibernateUtil.inTransaction(session -> session.merge(objectId));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectRegionalAuthorities regionalAuthorities = session.get(ObjectRegionalAuthorities.class, id);
            if (regionalAuthorities != null) {
                session.remove(regionalAuthorities);
            }
        });
    }
}
