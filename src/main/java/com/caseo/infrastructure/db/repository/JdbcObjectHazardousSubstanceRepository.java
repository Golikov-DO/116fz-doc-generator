package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousSubstance;
import com.caseo.domain.repository.ObjectHazardousSubstanceRepository;

import java.sql.SQLException;
import java.util.List;

public class JdbcObjectHazardousSubstanceRepository extends BaseJdbcRepository<ObjectHazardousSubstance> implements ObjectHazardousSubstanceRepository {

    @Override
    protected String table() {
        return "hazardous_substance";
    }

    protected RowMapper<ObjectHazardousSubstance> mapper() {
        return rs -> new ObjectHazardousSubstance(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("name_gen")
        );
    }

    @Override
    public ObjectHazardousSubstance findById(int objectId) {

        return findOne("id = ?", objectId).orElse(null);

    }

    @Override
    public List<ObjectHazardousSubstance> findAll() {
        return findList(null);
    }

    @Override
    public void save(ObjectHazardousSubstance objectHazardousSubstance, int objectId) throws SQLException {

    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("id = ?", objectId);
    }
}