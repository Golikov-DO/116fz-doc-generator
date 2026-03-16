package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Asf;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfRepository implements ParentRepository<Asf> {
    @Override
    public Asf findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(Asf.class, id)
        );
    }

    @Override
    public List<Asf> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from Asf order by shortName", Asf.class)
                        .list()
        );
    }

    @Override
    public void save(Asf asf) {
        HibernateUtil.inTransaction(session -> {
            if (asf.getId() == null) {
                session.persist(asf);  // persist присваивает ID
            } else {
                session.merge(asf);     // merge для обновления
            }
        });
    }
    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            Asf asf = session.get(Asf.class, id);
            if (asf != null) {
                session.remove(asf);
            }
        });
    }
}
