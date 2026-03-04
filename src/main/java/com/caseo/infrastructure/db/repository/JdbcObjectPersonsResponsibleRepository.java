package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectPersonsResponsible;
import com.caseo.domain.repository.ObjectPersonsResponsibleRepository;

import java.sql.SQLException;
import java.util.List;

public class JdbcObjectPersonsResponsibleRepository extends BaseJdbcRepository<ObjectPersonsResponsible> implements ObjectPersonsResponsibleRepository {

    @Override
    protected String table() {
        return "object_responsible_persons";
    }

    @Override
    protected RowMapper<ObjectPersonsResponsible> mapper() {
        return rs -> new ObjectPersonsResponsible(
                rs.getInt("object_id"),
                rs.getInt("number"),
                rs.getString("full_name"),
                rs.getString("position")
        );
    }

    @Override
    public List<ObjectPersonsResponsible> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }

    @Override
    public void save(ObjectPersonsResponsible objectPersonsResponsible, int objectId) throws SQLException {

    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}