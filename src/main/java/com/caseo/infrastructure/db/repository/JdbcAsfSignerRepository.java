package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.AsfSignerRepository;

public class JdbcAsfSignerRepository extends BaseJdbcRepository<AsfSigner> implements AsfSignerRepository {

    @Override
    protected String table() {
        return "asf_signer";
    }

    @Override
    protected RowMapper<AsfSigner> mapper() {
        return rs -> new AsfSigner(
                rs.getString("signer_name"),
                rs.getString("signer_position")
        );
    }

    @Override
    public AsfSigner findByAsfId(int asfId) {
        return findOne("asf_id = ? AND id = 2", asfId).orElse(null);
    }
}
