package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ReferenceTableTitle;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateReferenceTableTitleRepository implements ParentRepository<ReferenceTableTitle> {
    @Override
    public ReferenceTableTitle findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(ReferenceTableTitle.class, id)
        );
    }

    @Override
    public List<ReferenceTableTitle> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ReferenceTableTitle order by id", ReferenceTableTitle.class)
                        .list()
        );
    }

    @Override
    public void save(ReferenceTableTitle title) {
        HibernateUtil.inTransaction(session -> session.merge(title));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ReferenceTableTitle tableTitle = session.get(ReferenceTableTitle.class, id);
            if (tableTitle != null) {
                session.remove(tableTitle);
            }
        });
    }
}
