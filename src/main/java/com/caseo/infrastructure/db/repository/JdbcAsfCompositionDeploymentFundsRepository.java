package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;
import com.caseo.domain.repository.AsfCompositionDeploymentFundsRepository;

public class JdbcAsfCompositionDeploymentFundsRepository extends BaseJdbcRepository<AsfCompositionDeploymentFunds> implements AsfCompositionDeploymentFundsRepository {

    @Override
    protected String table() {
        return "asf_composition_deployment_funds";
    }

    @Override
    protected RowMapper<AsfCompositionDeploymentFunds> mapper() {
        return rs -> new AsfCompositionDeploymentFunds(
                rs.getString("responsibility_area"),
                rs.getString("deployment_Place"),
                rs.getString("duty_officer_telephone"),
                rs.getString("contact_telephone"),
                rs.getString("e_mail"),
                rs.getString("number_buildings"),
                rs.getString("total_area")
        );
    }

    @Override
    public AsfCompositionDeploymentFunds findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }
}
