package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ReferenceCity;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;
import java.util.List;

public class HibernateReferenceCityRepository implements ParentRepository<ReferenceCity> {
    
    @Override
    public ReferenceCity findOneById(int id) {
        return HibernateUtil.inSession(session -> 
            session.get(ReferenceCity.class, id)
        );
    }
    
    @Override
    public List<ReferenceCity> findMany() {
        return HibernateUtil.inSession(session -> 
            session.createQuery("from ReferenceCity order by cityName", ReferenceCity.class)
                   .list()
        );
    }

    @Override
    public void save(ReferenceCity city) {
        HibernateUtil.inTransaction(session -> session.merge(city));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ReferenceCity referenceCity = session.get(ReferenceCity.class, id);
            if (referenceCity != null) {
                session.remove(referenceCity);
            }
        });
    }
}