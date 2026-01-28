package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.*;
import com.caseo.domain.repository.AsfRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcAsfRepository implements AsfRepository {

    // ---------- mappers ----------

    private final RowMapper<Asf> asfMapper = rs ->
            new Asf(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("short_name"),
                    rs.getString("status"),
                    rs.getString("responsibility_zone"),
                    rs.getString("email"),
                    rs.getInt("buildings_count"),
                    rs.getString("notes"),
                    rs.getString("location_text"),
                    rs.getInt("document_set_id"),
                    rs.getString("status_short"),
                    rs.getString("status_gen")
            );

    private final RowMapper<AsfCertificate> certificateMapper = rs ->
            new AsfCertificate(
                    rs.getInt("id"),
                    rs.getString("cert_number"),
                    rs.getString("cert_series"),
                    rs.getString("issued_by"),
                    rs.getString("issue_basis"),
                    rs.getString("issue_date"),
                    rs.getString("valid_until")
            );

    private final RowMapper<AsfWorkType> workTypeMapper = rs ->
            new AsfWorkType(
                    rs.getInt("id"),
                    rs.getString("name")
            );

    // ---------- queries ----------

    @Override
    public Asf findByDocumentSet(int documentSetId) {

        String asfSql = """
            SELECT 
                id,
                full_name,
                short_name,
                status,
                responsibility_zone,
                email,
                buildings_count,
                notes,
                location_text,
                document_set_id,
                status_short,
                status_gen
            FROM asf
            WHERE document_set_id = ?
            LIMIT 1
        """;

        Asf asf = DbUtils.queryOne(asfSql, asfMapper, documentSetId);

        if (asf == null) return null;

        // ---------- certificate ----------
        String certSql = """
            SELECT 
                id,
                cert_number,
                cert_series,
                issued_by,
                issue_basis,
                issue_date,
                valid_until
            FROM asf_certificate
            WHERE asf_id = ?
            LIMIT 1
        """;

        AsfCertificate cert =
                DbUtils.queryOne(certSql, certificateMapper, asf.getId());

        asf.setCertificate(cert);

        // ---------- work types ----------
        String workSql = """
            SELECT wt.id, wt.name
            FROM asf_work_type wt
            JOIN asf_work_link wl ON wl.work_type_id = wt.id
            WHERE wl.asf_id = ?
        """;

        List<AsfWorkType> types =
                DbUtils.queryMany(workSql, workTypeMapper, asf.getId());

        asf.setWorkTypes(types);

        return asf;
    }
}