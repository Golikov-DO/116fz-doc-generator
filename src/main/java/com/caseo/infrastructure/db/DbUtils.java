package com.caseo.infrastructure.db;

import com.caseo.infrastructure.db.repository.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    public static <T> T queryOne(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapper.map(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> queryMany(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapper.map(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}