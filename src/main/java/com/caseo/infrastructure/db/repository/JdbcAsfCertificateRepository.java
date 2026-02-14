package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfCertificate;
import com.caseo.domain.repository.AsfCertificateRepository;

public class JdbcAsfCertificateRepository extends BaseJdbcRepository<AsfCertificate> implements AsfCertificateRepository {

    @Override
    protected String table() {
        return "asf_certificate";
    }

    @Override
    protected RowMapper<AsfCertificate> mapper() {
        return rs -> new AsfCertificate(
                rs.getString("cert_number"),
                rs.getString("cert_series"),
                rs.getString("issued_by"),
                rs.getString("issue_basis"),
                rs.getString("issue_date"),
                rs.getString("valid_until")
        );
    }

    @Override
    public AsfCertificate findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }
}
