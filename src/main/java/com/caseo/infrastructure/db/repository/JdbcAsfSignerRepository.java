package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.AsfSignerRepository;
import com.caseo.infrastructure.db.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAsfSignerRepository extends BaseJdbcRepository<AsfSigner> implements AsfSignerRepository {

    @Override
    protected String table() {
        return "asf_signer";
    }

    @Override
    protected String idColumn() {
        return "id";
    }

    @Override
    protected RowMapper<AsfSigner> mapper() {
        return rs -> new AsfSigner(
                rs.getInt("id"),
                rs.getInt("asf_id"),
                rs.getString("signer_name"),
                rs.getString("signer_position")
        );
    }

    @Override
    public AsfSigner findByAsfId(int asfId) {
        String sql = "SELECT * FROM asf_signer WHERE asf_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, asfId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapper().map(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("DB error in findByAsfId(asf_id=" + asfId + ")", e);
        }
    }
}
