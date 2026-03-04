package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectInsurancePolicy;
import com.caseo.domain.repository.ObjectInsurancePolicyRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcObjectInsurancePolicyRepository extends BaseJdbcRepository<ObjectInsurancePolicy>
        implements ObjectInsurancePolicyRepository {

    @Override
    protected String table() {
        return "object_insurance_policy";
    }

    @Override
    protected RowMapper<ObjectInsurancePolicy> mapper() {
        return rs -> new ObjectInsurancePolicy(
                rs.getInt("object_id"),
                rs.getString("number"),
                rs.getString("valid_until")
        );
    }

    @Override
    public ObjectInsurancePolicy findByObjectId(int objectId) {
        return findOne("object_id = ?", objectId).orElse(null);
    }

    @Override
    public void save(ObjectInsurancePolicy objectInsurancePolicy, int objectId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("number", objectInsurancePolicy.number());
        String validUntil = objectInsurancePolicy.validUntil();
        if (validUntil == null || validUntil.trim().isEmpty()) {
            data.put("valid_until", null);
        } else {
            // Чтобы избежать ошибки "expression is of type character varying",
            // лучше передать объект LocalDate, а не строку
            data.put("valid_until", java.time.LocalDate.parse(validUntil));
        }

        insert(data);
    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}
