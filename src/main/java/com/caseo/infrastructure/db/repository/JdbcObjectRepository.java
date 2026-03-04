package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.repository.ObjectRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcObjectRepository extends BaseJdbcRepository<ObjectModel> implements ObjectRepository {

    @Override
    protected String table() {
        return "object";
    }

    @Override
    protected RowMapper<ObjectModel> mapper() {
        return rs -> new ObjectModel(
                rs.getInt("id"),
                rs.getInt("org_id"),
                rs.getInt("asf_id"),
                rs.getInt("asf_signer_id"),
                rs.getInt("object_city_id"),
                rs.getInt("hazardous_substance_id"),
                rs.getInt("hazard_class"),
                rs.getString("full_name"),
                rs.getString("amount_of_hazardous_substance"),
                rs.getString("nearest_fire_station"),
                rs.getString("short_name"),
                rs.getString("department_gochs_city"),
                rs.getBoolean("emergency_commission")
        );
    }

    @Override
    public ObjectModel findByOrgId(int orgId) {
        return findOne("org_id = ?", orgId).orElse(null);
    }

    @Override
    public ObjectModel findById(int id) {
        return findOne("id = ?", id).orElse(null);
    }

    @Override
    public List<ObjectModel> findAllByOrgId(int orgId) {
        return findList("org_id = ?", orgId);
    }

    @Override
    public ObjectModel save(ObjectModel object) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("org_id", object.orgId());
        data.put("asf_id", object.asfId());
        data.put("asf_signer_id", object.asf_signer_id());
        data.put("object_city_id", object.object_city_id());
        data.put("hazardous_substance_id", object.hazardousSubstanceId());
        data.put("hazard_class", object.hazardClass());
        data.put("full_name", object.objectFullName());
        data.put("amount_of_hazardous_substance", object.amountOfHazardousSubstance());
        data.put("nearest_fire_station", object.nearestFireStation());
        data.put("short_name", object.objectShortName());
        data.put("department_gochs_city", object.departmentGoChsCity());
        data.put("emergency_commission", object.emergencyCommission());

        if (object.id() == 0) {
            int newId = insert(data);
            return new ObjectModel(
                    newId,
                    object.orgId(),
                    object.asfId(),
                    object.asf_signer_id(),
                    object.object_city_id(),
                    object.hazardousSubstanceId(),
                    object.hazardClass(),
                    object.objectFullName(),
                    object.amountOfHazardousSubstance(),
                    object.nearestFireStation(),
                    object.objectShortName(),
                    object.departmentGoChsCity(),
                    object.emergencyCommission()
            );
        } else {
            String[] fields = data.keySet().toArray(new String[0]);
            Object[] values = data.values().toArray();
            update(fields, values, "id = ?", object.id());
            return object;
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        delete("id = ?", id);
    }
}