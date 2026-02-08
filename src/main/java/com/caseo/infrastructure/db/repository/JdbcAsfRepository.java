package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.*;
import com.caseo.domain.repository.AsfRepository;

public class JdbcAsfRepository extends BaseJdbcRepository<Asf> implements AsfRepository {

    @Override
    protected String table() {
        return "asf";
    }

    protected RowMapper<Asf> mapper() {
        return rs -> new Asf(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("full_name_gen"),
                rs.getString("short_name"),
                rs.getString("status"),
                rs.getString("responsibility_zone"),
                rs.getString("email"),
                rs.getInt("buildings_count"),
                rs.getString("notes"),
                rs.getString("location_text"),
                rs.getString("status_short"),
                rs.getString("status_gen"),
                rs.getString("arrival_time"),
                rs.getString("telephone")
        );
    }

    @Override
    public Asf findByOrganizationId(int organizationId) {

        return findOne(
                "JOIN organization ON organization.asf_id = asf.id",
                "organization.id = ?",
                organizationId
        ).orElse(null);
    }
}