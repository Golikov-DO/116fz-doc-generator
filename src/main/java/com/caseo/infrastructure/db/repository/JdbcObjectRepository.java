package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.repository.ObjectRepository;

public class JdbcObjectRepository extends BaseJdbcRepository<ObjectModel> implements ObjectRepository {

    @Override
    protected String table() {
        return "object";
    }

    @Override
    protected RowMapper<ObjectModel> mapper() {
        return rs -> new ObjectModel(
                        rs.getInt("id"),
                        rs.getInt("org_id"),
                        rs.getInt("hazardous_substance_id"),
                        rs.getInt("hazard_class"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("amount_of_hazardous_substance"),
                        rs.getString("nearest_fire_station"),
                        rs.getString("short_name"),
                        rs.getString("department_gochs_city")
        );
    }

    @Override
    public ObjectModel findByOrgId(int orgId) {
        return findOne("org_id = ?", orgId).orElse(null);
    }
}