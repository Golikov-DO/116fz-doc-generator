package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Organization;
import com.caseo.domain.repository.OrganizationRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                rs.getString("organization_type_activity"),
                rs.getBoolean("one_territory")
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

    public Organization save(Organization organization) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("organization_full_name", organization.organizationName());
        data.put("organization_short_name", organization.organizationShortName());
        data.put("organization_type_activity", organization.organizationTypeActivity());
        data.put("one_territory", organization.oneTerritory());

        if (organization.organizationId() == 0) {
            // CREATE - вставка новой записи
            int newId = insert(data);
            return new Organization(
                    newId,
                    organization.organizationName(),
                    organization.organizationShortName(),
                    organization.organizationTypeActivity(),
                    organization.oneTerritory()
            );
        } else {
            // EDIT - обновление существующей
            String[] fields = data.keySet().toArray(new String[0]);
            Object[] values = data.values().toArray();

            update(fields, values, "id = ?", organization.organizationId());

            // возвращаем тот же объект с существующим ID
            return organization;
        }
    }
}
