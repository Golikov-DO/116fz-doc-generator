package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationContact;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateOrganizationContactRepository implements ChildRepository<OrganizationContact> {

    @Override
    public OrganizationContact findOneByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from OrganizationContact where organization.id = :orgId",
                                OrganizationContact.class)
                        .setParameter("orgId", orgId)
                        .uniqueResultOptional()
                        .orElse(null)
        );
    }

    @Override
    public List<OrganizationContact> findManyByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from OrganizationContact where organization.id = :orgId",
                                OrganizationContact.class)
                        .setParameter("orgId", orgId)
                        .list()
        );
    }

    @Override
    public void save(OrganizationContact organizationContact) {
        HibernateUtil.inTransaction(session -> {
            if (organizationContact.getId() == null) {  // Проверка на null
                session.persist(organizationContact);
            } else {
                session.merge(organizationContact);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            OrganizationContact organizationContact = session.get(OrganizationContact.class, id);
            if (organizationContact != null) {
                session.remove(organizationContact);
            }
        });
    }
}
