package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfCertificate;
import com.caseo.domain.repository.AsfCertificateRepository;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public void save(AsfCertificate certificate, int asfId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("asf_id", asfId);
        data.put("cert_number", certificate.certNumber());
        data.put("cert_series", certificate.certSeries());
        data.put("issued_by", certificate.issuedBy());
        data.put("issue_basis", certificate.issueBasis());
        data.put("issue_date", certificate.issueDate());
        data.put("valid_until", certificate.validUntil());

        insert(data);
    }

    public void deleteByAsfId(int asfId) {
        delete("asf_id = ?", asfId);
    }
}
