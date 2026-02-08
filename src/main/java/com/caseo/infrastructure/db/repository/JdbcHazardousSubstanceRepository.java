package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.HazardousSubstance;
import com.caseo.domain.repository.HazardousSubstanceRepository;

public class JdbcHazardousSubstanceRepository extends BaseJdbcRepository<HazardousSubstance> implements HazardousSubstanceRepository {

    @Override
    protected String table() {
        return "hazardous_substance";
    }

    protected RowMapper<HazardousSubstance> mapper() {
        return rs -> new HazardousSubstance(
        rs.getInt("id"),
        rs.getInt("object_id"),
        rs.getString("name"),
        rs.getString("name_gen")
        );
    }

    @Override
    public HazardousSubstance findById(int objectId) {

        return  findOne("object_id = ?", objectId).orElse(null);

    }
}