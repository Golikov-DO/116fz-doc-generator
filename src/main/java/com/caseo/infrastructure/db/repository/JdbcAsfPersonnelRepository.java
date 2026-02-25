package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfPersonnel;
import com.caseo.domain.repository.AsfPersonnelRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcAsfPersonnelRepository extends BaseJdbcRepository<AsfPersonnel> implements AsfPersonnelRepository {

    @Override
    protected String table() {
        return "asf_personnel";
    }

    @Override
    protected RowMapper<AsfPersonnel> mapper() {
        return rs -> new AsfPersonnel(
                rs.getInt("staff_by_staffing"),
                rs.getInt("staff_by_list"),
                rs.getInt("certified_total"),
                rs.getInt("qualified_total"),
                rs.getInt("third_class"),
                rs.getInt("second_class"),
                rs.getInt("first_class"),
                rs.getInt("international_class")
        );
    }

    @Override
    public AsfPersonnel findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }

    @Override
    public void save(AsfPersonnel asfPersonnel, int asfId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("asf_id", asfId);
        data.put("staff_by_staffing", asfPersonnel.staffByStaffing());
        data.put("staff_by_list", asfPersonnel.staffByList());
        data.put("certified_total", asfPersonnel.certifiedTotal());
        data.put("qualified_total", asfPersonnel.qualifiedTotal());
        data.put("third_class", asfPersonnel.thirdClass());
        data.put("second_class", asfPersonnel.secondClass());
        data.put("first_class", asfPersonnel.firstClass());
        data.put("international_class", asfPersonnel.internationalClass());

        insert(data);
    }

    @Override
    public void deleteByAsfId(int asfId) {
        delete("asf_id = ?", asfId);
    }
}
