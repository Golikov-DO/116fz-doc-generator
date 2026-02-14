package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ReferenceEmergencyServices;
import com.caseo.domain.repository.ReferenceEmergencyServicesRepository;

import java.util.List;

public class JdbcReferenceEmergencyServicesRepository extends BaseJdbcRepository<ReferenceEmergencyServices> implements ReferenceEmergencyServicesRepository {

    @Override
    protected String table() {
        return "emergency_services";
    }

    @Override
    protected RowMapper<ReferenceEmergencyServices> mapper() {
        return rs -> new ReferenceEmergencyServices(
                rs.getInt("id"),
                rs.getString("service_name"),
                rs.getString("service_name"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }

    @Override
    public List<ReferenceEmergencyServices> findAll() {
        // Передаем null в where, чтобы SQL был без фильтрации
        return findList(null);
    }
}
