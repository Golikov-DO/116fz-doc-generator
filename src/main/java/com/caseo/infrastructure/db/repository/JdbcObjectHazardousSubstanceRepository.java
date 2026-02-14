package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousSubstance;
import com.caseo.domain.repository.ObjectHazardousSubstanceRepository;

public class JdbcObjectHazardousSubstanceRepository extends BaseJdbcRepository<ObjectHazardousSubstance> implements ObjectHazardousSubstanceRepository {

    @Override
    protected String table() {
        return "hazardous_substance";
    }

    protected RowMapper<ObjectHazardousSubstance> mapper() {
        return rs -> new ObjectHazardousSubstance(
                rs.getInt("id"),
                rs.getInt("object_id"),
                rs.getString("name"),
                rs.getString("name_gen")
        );
    }

    @Override
    public ObjectHazardousSubstance findById(int objectId) {

        return findOne("object_id = ?", objectId).orElse(null);

    }
}