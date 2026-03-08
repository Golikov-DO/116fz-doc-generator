package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ReferenceEmergencyServices;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateReferenceEmergencyServicesRepository implements ParentRepository<ReferenceEmergencyServices> {
    @Override
    public ReferenceEmergencyServices findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(ReferenceEmergencyServices.class, id)
        );
    }

    @Override
    public List<ReferenceEmergencyServices> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ReferenceEmergencyServices order by id", ReferenceEmergencyServices.class)
                        .list()
        );
    }

    @Override
    public void save(ReferenceEmergencyServices service) {
        HibernateUtil.inTransaction(session -> session.merge(service));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ReferenceEmergencyServices emergencyServices = session.get(ReferenceEmergencyServices.class, id);
            if (emergencyServices != null) {
                session.remove(emergencyServices);
            }
        });
    }
}
