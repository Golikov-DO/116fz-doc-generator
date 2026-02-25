package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;
import com.caseo.domain.repository.AsfCompositionDeploymentFundsRepository;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public void save(AsfCompositionDeploymentFunds asfCompositionDeploymentFunds, int asfId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("asf_id", asfId);
        data.put("responsibility_area", asfCompositionDeploymentFunds.responsibilityArea());
        data.put("duty_officer_telephone", asfCompositionDeploymentFunds.dutyOfficerTelephone());
        data.put("contact_telephone", asfCompositionDeploymentFunds.contactTelephone());
        data.put("e_mail", asfCompositionDeploymentFunds.eMail());
        data.put("number_buildings", asfCompositionDeploymentFunds.numberBuildings());
        data.put("total_area", asfCompositionDeploymentFunds.totalArea());

        insert(data);
    }

    public void deleteByAsfId(int asfId) {
        delete("asf_id = ?", asfId);
    }
}
