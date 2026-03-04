package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrganizationSigner;
import com.caseo.domain.repository.OrganizationSignerRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcOrganizationSignerRepository extends BaseJdbcRepository<OrganizationSigner> implements OrganizationSignerRepository {

    @Override
    protected String table() {
        return "organization_signer";
    }

    @Override
    protected RowMapper<OrganizationSigner> mapper() {
        return rs -> new OrganizationSigner(
                rs.getInt("id"),
                rs.getInt("organization_id"),
                rs.getString("signer_surname_basic"),
                rs.getString("signer_position")
        );
    }

    @Override
    public OrganizationSigner findByOrganizationId(int orgId) {
        return findOne("organization_id = ?", orgId).orElse(null);
    }

    @Override
    public void save(OrganizationSigner organizationSigner, int orgId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("organization_id", orgId);
        data.put("signer_surname_basic", organizationSigner.name());
        data.put("signer_position", organizationSigner.position());

        // Проверяем, есть ли уже запись
        OrganizationSigner existing = findByOrganizationId(orgId);

        if (existing == null) {
            insert(data);
        } else {
            String[] fields = data.keySet().toArray(new String[0]);
            Object[] values = data.values().toArray();
            update(fields, values, "organization_id = ?", orgId);
        }
    }

    public void deleteByOrganizationId(int organizationId) {
        delete("organization_id = ?", organizationId);
    }
}
