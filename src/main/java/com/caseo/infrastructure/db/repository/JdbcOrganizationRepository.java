package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Organization;
import com.caseo.domain.repository.OrganizationRepository;

import java.util.List;

public class JdbcOrganizationRepository extends BaseJdbcRepository<Organization>
        implements OrganizationRepository {

    @Override
    protected String table() {
        return "organization";
    }

    @Override
    protected RowMapper<Organization> mapper() {
        return rs -> new Organization(
                rs.getInt("id"),
                rs.getString("organization_full_name"),
                rs.getString("organization_short_name"),
                rs.getInt("asf_id"),
                rs.getString("organization_type_activity")
        );
    }

    @Override
    public Organization findById(int id) {
        return findOne("id = ?", id).orElse(null);
    }

    @Override
    public List<Organization> findAll() {
        return findList(null);
    }
}
