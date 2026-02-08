package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationSigner;
import com.caseo.domain.repository.OrganizationSignerRepository;

public class JdbcOrganizationSignerRepository extends BaseJdbcRepository<OrganizationSigner> implements OrganizationSignerRepository {

    @Override
    protected String table() {
        return "organization_signer";
    }

    @Override
    protected RowMapper<OrganizationSigner> mapper() {
        return rs -> new OrganizationSigner(
                rs.getInt("id"),
                rs.getInt("org_id"),
                rs.getString("signer_surname_basic"),
                rs.getString("signer_position")
        );
    }

    @Override
    public OrganizationSigner findByOrganizationId(int orgId) {
        return findOne("org_id = ?", orgId).orElse(null);
    }
}
