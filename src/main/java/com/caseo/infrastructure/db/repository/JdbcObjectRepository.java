package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.repository.ObjectRepository;
import com.caseo.infrastructure.db.DbUtils;

public class JdbcObjectRepository implements ObjectRepository {

    // --- mapper ---
    private final RowMapper<ObjectModel> mapper = rs ->
            new ObjectModel(
                    rs.getInt("id"),
                    rs.getInt("org_id"),
                    rs.getInt("hazardous_substance_id"),
                    rs.getBytes("plan_and_diagram_OPO"),
                    rs.getString("object_name"),
                    rs.getString("object_address"),
                    rs.getString("amount_of_hazardous_substance"),
                    rs.getInt("hazard_class")
            );

    @Override
    public ObjectModel findByOrgId(int orgId) {

        String sql = """
            SELECT id, org_id, hazardous_substance_id, plan_and_diagram_OPO,  
                   object_name, object_address, amount_of_hazardous_substance, 
                   hazard_class
            FROM object
            WHERE org_id = ?
            LIMIT 1
        """;

        return DbUtils.queryOne(sql, mapper, orgId);
    }
}