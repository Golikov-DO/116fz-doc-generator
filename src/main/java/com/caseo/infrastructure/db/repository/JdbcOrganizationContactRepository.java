package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationContact;
import com.caseo.domain.repository.OrganizationContactRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public List<OrganizationContact> findByOrganizationId(int orgId) {
        return findList("organization_id = ?", orgId);
    }

    @Override
    public void save(OrganizationContact organizationContact, int orgId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("organization_id", organizationContact.organizationId());
        data.put("full_name", organizationContact.fullName());
        data.put("position", organizationContact.position());
        data.put("phones", organizationContact.phones());
        data.put("address", organizationContact.address());

        insert(data);
    }

    public void deleteByOrganizationId(int organizationId) {
        delete("organization_id = ?", organizationId);
    }
}
