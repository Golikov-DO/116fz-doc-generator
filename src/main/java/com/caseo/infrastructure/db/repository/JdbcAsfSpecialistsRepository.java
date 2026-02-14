package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSpecialists;
import com.caseo.domain.repository.AsfSpecialistsRepository;

public class JdbcAsfSpecialistsRepository extends BaseJdbcRepository<AsfSpecialists> implements AsfSpecialistsRepository {

    @Override
    protected String table() {
        return "asf_specialists";
    }

    @Override
    protected RowMapper<AsfSpecialists> mapper() {
        return rs -> new AsfSpecialists(
                rs.getInt("total_count"),
                rs.getInt("asr_tp"),
                rs.getInt("asr_lrn_ter"),
                rs.getInt("gzsr"),
                rs.getInt("psr"),
                rs.getInt("driver"),
                rs.getInt("asr_lrn_sea")
        );
    }

    @Override
    public AsfSpecialists findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }
}
