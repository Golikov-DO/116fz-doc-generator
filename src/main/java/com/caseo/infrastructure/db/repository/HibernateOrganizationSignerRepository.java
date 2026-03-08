package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationSigner;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateOrganizationSignerRepository implements ChildRepository<OrganizationSigner> {

    @Override
    public OrganizationSigner findOneByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from OrganizationSigner where organization.id = :orgId",
                                OrganizationSigner.class)
                        .setParameter("orgId", orgId)
                        .uniqueResultOptional()
                        .orElse(null)  // Просто возвращаем null, если нет подписанта
        );
    }

    @Override
    public List<OrganizationSigner> findManyByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from OrganizationSigner where organization.id = :orgId",
                                OrganizationSigner.class)
                        .setParameter("orgId", orgId)
                        .list()
        );
    }

    @Override
    public void save(OrganizationSigner entity) {
        HibernateUtil.inTransaction(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                session.merge(entity);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            OrganizationSigner organizationSigner = session.get(OrganizationSigner.class, id);
            if (organizationSigner != null) {
                session.remove(organizationSigner);
            }
        });
    }
}