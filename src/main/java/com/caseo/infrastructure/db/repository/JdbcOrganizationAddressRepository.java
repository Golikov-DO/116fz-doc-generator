package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationAddress;
import com.caseo.domain.repository.OrganizationAddressRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcOrganizationAddressRepository extends BaseJdbcRepository<OrganizationAddress> implements OrganizationAddressRepository {

    @Override
    protected String table() {
        return "organization_address";
    }

    @Override
    protected RowMapper<OrganizationAddress> mapper() {
        return rs -> new OrganizationAddress(
                rs.getInt("organization_id"),
                rs.getObject("index") != null ? rs.getInt("index") : 0,
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

    @Override
    public void save(OrganizationAddress organizationAddress, int orgId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("organization_id", orgId);
        data.put("index", organizationAddress.index());
        data.put("constituent_entity", organizationAddress.constituentEntity());
        data.put("area_hierarchy", organizationAddress.areaHierarchy());
        data.put("city_name", organizationAddress.city());
        data.put("street", organizationAddress.street());
        data.put("house", organizationAddress.house());
        data.put("raw_address", organizationAddress.rawAddress());

        // Проверяем, есть ли уже запись для этой организации
        OrganizationAddress existing = findByOrganizationId(orgId);

        if (existing == null) {
            // Нет записи - INSERT
            insert(data);
        } else {
            // Есть запись - UPDATE
            String[] fields = data.keySet().toArray(new String[0]);
            Object[] values = data.values().toArray();
            update(fields, values, "organization_id = ?", orgId);
        }
    }

    public void deleteByOrganizationId(int organizationId) {
        delete("organization_id = ?", organizationId);
    }
}
