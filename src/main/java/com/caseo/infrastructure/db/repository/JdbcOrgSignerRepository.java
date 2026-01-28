package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.OrgSigner;
import com.caseo.domain.repository.OrgSignerRepository;
import com.caseo.infrastructure.db.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcOrgSignerRepository extends BaseJdbcRepository<OrgSigner> implements OrgSignerRepository {

    @Override
    protected String table() {
        return "id";
    }

    @Override
    protected String idColumn() {
        return "org_signer";
    }

    @Override
    protected RowMapper<OrgSigner> mapper() {
        return rs -> new OrgSigner(
                rs.getInt("id"),
                rs.getInt("org_id"),
                rs.getString("signer_surname_basic"),
                rs.getString("signer_position")
        );
    }

    @Override
    public OrgSigner findByOrganizationId(int orgId) {
        String sql = "SELECT * FROM org_signer WHERE org_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orgId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapper().map(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("DB error in findByAsfId(asf_id=" + orgId + ")", e);
        }
    }
}
