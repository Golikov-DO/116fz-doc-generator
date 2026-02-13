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
                rs.getString("geo_coords"),
                rs.getString("geo_relief"),
                rs.getString("geo_geology"),
                rs.getString("climat_desc"),
                rs.getString("hydro_desc"),
                rs.getString("infra_transport"),
                rs.getString("infra_engineering"),
                rs.getString("infra_organizations"),
                rs.getString("nearby_towns"),
                rs.getString("mass_people_places"),
                rs.getString("admin_status"),
                rs.getString("dist_centers"),
                rs.getString("city_name")
        );
    }

    @Override
    public ObjectCity findById(int id) {
        List<ObjectCity> list = findList("id = ?", id);
        return list.isEmpty() ? null : list.getFirst();
    }
}
