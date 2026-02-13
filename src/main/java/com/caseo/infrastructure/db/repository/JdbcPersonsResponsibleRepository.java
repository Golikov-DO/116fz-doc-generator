package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.PersonsResponsible;
import com.caseo.domain.repository.PersonsResponsibleRepository;

import java.util.List;

public class JdbcPersonsResponsibleRepository extends BaseJdbcRepository<PersonsResponsible> implements PersonsResponsibleRepository {

    @Override
    protected String table() {
        return "responsible_persons";
    }

    @Override
    protected RowMapper<PersonsResponsible> mapper() {
        return rs -> new PersonsResponsible(
                rs.getInt("id"),
                rs.getInt("number"),
                rs.getString("full_name"),
                rs.getString("position")
        );
    }

    @Override
    public List<PersonsResponsible> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}