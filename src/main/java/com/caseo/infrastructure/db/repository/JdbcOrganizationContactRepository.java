package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationContact;
import com.caseo.domain.repository.OrganizationContactRepository;

import java.util.List;

public class JdbcOrganizationContactRepository extends BaseJdbcRepository<OrganizationContact> implements OrganizationContactRepository {

    @Override
    protected String table() {
        return "organization_contact";
    }

    @Override
    protected RowMapper<OrganizationContact> mapper() {
        return rs -> new OrganizationContact(
                rs.getInt("organization_id"),
                rs.getString("full_name"),
                rs.getString("position"),
                rs.getString("phones"),
                rs.getString("address")
        );
    }

    @Override
    public List<OrganizationContact> findByOrganizationId(int organizationId) {
        return findList("organization_id = ?", organizationId);
    }
}
