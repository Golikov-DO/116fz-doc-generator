package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ReferenceCity;
import com.caseo.domain.repository.ReferenceCityRepository;

import java.util.List;

public class JdbcReferenceCityRepository extends BaseJdbcRepository<ReferenceCity> implements ReferenceCityRepository {

    @Override
    protected String table() {
        return "object_city";
    }

    @Override
    protected RowMapper<ReferenceCity> mapper() {
        return rs -> new ReferenceCity(
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
    public ReferenceCity findById(int id) {
        List<ReferenceCity> list = findList("id = ?", id);
        return list.isEmpty() ? null : list.getFirst();
    }

    @Override
    public List<ReferenceCity> findAll() {
        return findList(null);
    }
}
