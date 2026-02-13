package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectInsurancePolicy;
import com.caseo.domain.repository.ObjectInsurancePolicyRepository;

public class JdbcObjectInsurancePolicyRepository extends BaseJdbcRepository<ObjectInsurancePolicy>
        implements ObjectInsurancePolicyRepository {

    @Override
    protected String table() {
        return "object_insurance_policy";
    }

    @Override
    protected RowMapper<ObjectInsurancePolicy> mapper() {
        return rs -> new ObjectInsurancePolicy(
                rs.getInt("id"),
                rs.getInt("obj_id"),
                rs.getString("number"),
                rs.getString("valid_until")
        );
    }

    @Override
    public ObjectInsurancePolicy findByObjectId(int objectId) {
        return findOne("obj_id = ?", objectId).orElse(null);
    }
}
