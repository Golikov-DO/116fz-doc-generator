package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Organization;
import com.caseo.domain.repository.OrganizationRepository;

public class JdbcOrganizationRepository
        extends BaseJdbcRepository<Organization>
        implements OrganizationRepository {

    @Override
    protected String table() {
        return "organization";
    }

    @Override
    protected String idColumn() {
        return "id";
    }

    @Override
    protected RowMapper<Organization> mapper() {
        return rs -> new Organization(
                rs.getString("organization_full_name"),
                rs.getString("organization_short_name"),
                rs.getString("organization_address"),
                rs.getInt("id")
        );
    }

    // кастомные методы
    @Override
    public Organization findById(int id) {
        return super.findOptionalById(id).orElse(null);
    }
}
