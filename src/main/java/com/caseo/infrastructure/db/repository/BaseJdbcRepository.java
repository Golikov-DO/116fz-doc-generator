package com.caseo.infrastructure.db.repository;

import com.caseo.infrastructure.db.DatabaseService;

import java.sql.*;
import java.util.*;

public abstract class BaseJdbcRepository<T> {

    protected abstract String table();
    protected abstract String idColumn();
    protected abstract RowMapper<T> mapper();

    protected Connection getConnection() throws SQLException {
        return DatabaseService.getConnection();
    }

    protected Optional<T> findOptionalById(int id) {
        String sql = "SELECT * FROM " + table() + " WHERE " + idColumn() + " = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapper().map(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("DB error in findById: " + table(), e);
        }
    }
}