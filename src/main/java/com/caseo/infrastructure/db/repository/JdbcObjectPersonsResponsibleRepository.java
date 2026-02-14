package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectPersonsResponsible;
import com.caseo.domain.repository.ObjectPersonsResponsibleRepository;

import java.util.List;

public class JdbcObjectPersonsResponsibleRepository extends BaseJdbcRepository<ObjectPersonsResponsible> implements ObjectPersonsResponsibleRepository {

    @Override
    protected String table() {
        return "responsible_persons";
    }

    @Override
    protected RowMapper<ObjectPersonsResponsible> mapper() {
        return rs -> new ObjectPersonsResponsible(
                rs.getInt("number"),
                rs.getString("full_name"),
                rs.getString("position")
        );
    }

    @Override
    public List<ObjectPersonsResponsible> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}