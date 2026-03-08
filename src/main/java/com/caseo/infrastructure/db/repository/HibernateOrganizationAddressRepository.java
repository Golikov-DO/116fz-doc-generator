package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationAddress;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateOrganizationAddressRepository implements ChildRepository<OrganizationAddress> {

    @Override
    public OrganizationAddress findOneByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from OrganizationAddress where organization.id = :orgId",
                                OrganizationAddress.class)
                        .setParameter("orgId", orgId)
                        .uniqueResultOptional()
                        .orElse(null)  // Теперь это работает, потому что мы убрали Objects.requireNonNull
        );
    }

    @Override
    public List<OrganizationAddress> findManyByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from OrganizationAddress where organization.id = :orgId",
                                OrganizationAddress.class)
                        .setParameter("orgId", orgId)
                        .list()
        );
    }

    @Override
    public void save(OrganizationAddress organizationAddress) {
        HibernateUtil.inTransaction(session -> {
            if (organizationAddress.getId() == null) {  // Новый объект
                session.persist(organizationAddress);
            } else {  // Существующий объект
                session.merge(organizationAddress);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            OrganizationAddress organizationAddress = session.get(OrganizationAddress.class, id);
            if (organizationAddress != null) {
                session.remove(organizationAddress);
            }
        });
    }
}