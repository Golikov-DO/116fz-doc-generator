package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.EmergencyServices;
import com.caseo.domain.repository.EmergencyServicesRepository;

import java.util.List;

public class JdbcEmergencyServicesRepository extends BaseJdbcRepository<EmergencyServices> implements EmergencyServicesRepository {

    @Override
    protected String table() {
        return "emergency_services";
    }

    @Override
    protected RowMapper<EmergencyServices> mapper() {
        return rs -> new EmergencyServices(
                rs.getInt("id"),
                rs.getString("service_name"),
                rs.getString("service_name"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }

    @Override
    public List<EmergencyServices> findAll() {
        // Передаем null в where, чтобы SQL был без фильтрации
        return findList(null);
    }
}
