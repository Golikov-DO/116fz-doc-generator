package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectCity;
import com.caseo.domain.repository.ObjectCityRepository;
import com.caseo.infrastructure.db.DbUtils;


public class JdbcObjectCityRepository implements ObjectCityRepository {

    // --- mapper ---
    private final RowMapper<ObjectCity> mapper = rs -> {
        ObjectCity c = new ObjectCity();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setRegion(rs.getString("region"));
        c.setCountryPart(rs.getString("country_part"));
        c.setStatus(rs.getString("status"));
        c.setAdminCenter(rs.getString("admin_center"));
        c.setFoundedYear(rs.getInt("founded_year"));
        c.setGeography(rs.getString("geography"));
        c.setDistanceInfo(rs.getString("distance_info"));
        c.setTransport(rs.getString("transport"));
        c.setClimate(rs.getString("climate"));
        c.setResortZone(rs.getString("resort_zone"));
        return c;
    };

    @Override
    public ObjectCity findById(int id) {
        String sql = """
            SELECT id, name, region, country_part,
                   status, admin_center, founded_year,
                   geography, distance_info, transport,
                   climate, resort_zone
            FROM object_city
            WHERE id = ?
        """;

        return DbUtils.queryOne(sql, mapper, id);
    }
}
