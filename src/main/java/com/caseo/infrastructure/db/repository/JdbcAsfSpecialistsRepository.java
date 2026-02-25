package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSpecialists;
import com.caseo.domain.repository.AsfSpecialistsRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Override
    public void save(AsfSpecialists asfSpecialists, int asfId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("asf_id", asfId);
        data.put("total_count", asfSpecialists.totalCount());
        data.put("asr_tp", asfSpecialists.asrTp());
        data.put("asr_lrn_ter", asfSpecialists.asrLrnTer());
        data.put("gzsr", asfSpecialists.gzsr());
        data.put("psr", asfSpecialists.psr());
        data.put("driver", asfSpecialists.driver());
        data.put("asr_lrn_sea", asfSpecialists.asrLrnSea());

        insert(data);
    }

    @Override
    public void deleteByAsfId(int asfId) throws SQLException {
        delete("asf_id = ?", asfId);
    }
}
