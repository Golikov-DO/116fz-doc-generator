package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSpecialists;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HubernateAsfSpecialistsRepository implements ChildRepository<AsfSpecialists> {
    @Override
    public AsfSpecialists findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.get(AsfSpecialists.class, asfId)
        );
    }

    @Override
    public List<AsfSpecialists> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfSpecialists where asf.id = :asfId order by id",
                                AsfSpecialists.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfSpecialists asfSpecialists) {
        HibernateUtil.inTransaction(session -> session.merge(asfSpecialists));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfSpecialists asfSpecialists = session.get(AsfSpecialists.class, id);
            if (asfSpecialists != null) {
                session.remove(asfSpecialists);
            }
        });
    }
}
