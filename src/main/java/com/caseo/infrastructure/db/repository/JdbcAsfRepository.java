package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Asf;
import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.AsfRepository;

import java.sql.SQLException;
import java.util.*;

public class JdbcAsfRepository extends BaseJdbcRepository<Asf> implements AsfRepository {

    @Override
    protected String table() {
        return "asf";
    }

    @Override
    protected RowMapper<Asf> mapper() {
        return rs -> new Asf(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("full_name_gen"),
                rs.getString("short_name"),
                rs.getString("email"),
                rs.getString("status_short"),
                rs.getString("arrival_time")
        );
    }

    @Override
    public Asf findById(int id) {
        return findOne("id = ?", id).orElse(null);
    }

    @Override
    public Asf findByObjectId(int objectId) {
        return findOne(
                "JOIN object ON object.asf_id = asf.asfId",
                "object.asfId = ?",
                objectId
        ).orElse(null);
    }

    @Override
    public List<AsfSigner> findAllByAsfId(int asfId) throws SQLException {
        return findAllByAsfId(asfId);
    }

    @Override
    public List<Asf> findAll() {
        return findList(null);
    }


    @Override
    public Asf save(Asf asf) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("full_name", asf.fullName());
        data.put("full_name_gen", asf.fullNameGen());
        data.put("short_name", asf.shortName());
        data.put("email", asf.email());
        data.put("status_short", asf.statusShort());

        // Преобразуем строку в java.sql.Time
        String timeStr = asf.arrivalTime();
        if (timeStr != null && !timeStr.trim().isEmpty()) {
            try {
                data.put("arrival_time", java.sql.Time.valueOf(timeStr));
            } catch (IllegalArgumentException e) {
                data.put("arrival_time", null);
            }
        } else {
            data.put("arrival_time", null);
        }

        int newId = insert(data);

        return new Asf(
                newId,
                asf.fullName(),
                asf.fullNameGen(),
                asf.shortName(),
                asf.email(),
                asf.statusShort(),
                asf.arrivalTime() // оставляем строку
        );
    }
}