package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Organization;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateOrganizationRepository implements ParentRepository<Organization> {
    @Override
    public Organization findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(Organization.class, id)
        );
    }

    @Override
    public List<Organization> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from Organization order by id", Organization.class)
                        .list()
        );
    }

    @Override
    public void save(Organization organization) {
        HibernateUtil.inTransaction(session -> {
            if (organization.getId() == null) {
                session.persist(organization);
            } else {
                session.merge(organization);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            Organization organization = session.get(Organization.class, id);
            if (organization != null) {
                session.remove(organization);
            }
        });
    }
}
