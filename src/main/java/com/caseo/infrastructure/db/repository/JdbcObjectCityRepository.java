package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectCity;
import com.caseo.domain.repository.ObjectCityRepository;

import java.util.List;


public class JdbcObjectCityRepository extends BaseJdbcRepository<ObjectCity> implements ObjectCityRepository {

    @Override
    protected String table() {
        return "object_city";
    }

    @Override
    protected RowMapper<ObjectCity> mapper() {
        return rs -> new ObjectCity(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("region"),
            rs.getString("country_part"),
            rs.getString("status"),
            rs.getString("admin_center"),
            rs.getInt("founded_year"),
            rs.getString("geography"),
            rs.getString("distance_info"),
            rs.getString("transport"),
            rs.getString("climate"),
            rs.getString("resort_zone")
        );
    }

    @Override
    public ObjectCity findByObjectId(int id) {
        List<ObjectCity> list = findList("id = ?", id);
        return list.isEmpty() ? null : list.getFirst();
    }
}
