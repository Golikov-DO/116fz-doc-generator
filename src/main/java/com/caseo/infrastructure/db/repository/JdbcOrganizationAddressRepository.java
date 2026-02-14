package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationAddress;
import com.caseo.domain.repository.OrganizationAddressRepository;

public class JdbcOrganizationAddressRepository extends BaseJdbcRepository<OrganizationAddress> implements OrganizationAddressRepository {

    @Override
    protected String table() {
        return "organization_address";
    }

    @Override
    protected RowMapper<OrganizationAddress> mapper() {
        return rs -> new OrganizationAddress(
                rs.getInt("id"),
                rs.getLong("organization_id"),
                rs.getObject("index") != null ? rs.getInt("index") : 0, // Безопасное получение int
                rs.getString("constituent_entity"),
                rs.getString("area_hierarchy"),
                rs.getString("city_name"),
                rs.getString("street"),
                rs.getString("house"),
                rs.getString("raw_address")
        );
    }

    @Override
    public OrganizationAddress findByOrganizationId(int organizationId) {
        return findOne("organization_id = ?", organizationId).orElse(null);
    }
}
