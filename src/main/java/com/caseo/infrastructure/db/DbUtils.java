package com.caseo.infrastructure.db;

import com.caseo.infrastructure.db.repository.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    // Существующий метод queryOne
    public static <T> T queryOne(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection conn = DatabaseSetService.getConnection();
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

    // Существующий метод queryMany
    public static <T> List<T> queryMany(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = new ArrayList<>();

        try (Connection conn = DatabaseSetService.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                preparedStatement.setObject(i + 1, params[i]);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(mapper.map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========== НОВЫЕ МЕТОДЫ ДЛЯ ЗАПИСИ ==========

    /**
     * Вставка записи с возвратом сгенерированного ID
     */
    public static int insert(String sql, Object... params) {
        try (Connection conn = DatabaseSetService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при вставке: " + sql, e);
        }
    }

    /**
     * Вставка записи БЕЗ возврата ID (если таблица не поддерживает RETURNING или GENERATED KEYS)
     */
    public static int insertWithoutKeys(String sql, Object... params) {
        try (Connection conn = DatabaseSetService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при вставке: " + sql, e);
        }
    }

    /**
     * Обновление записи
     */
    public static int update(String sql, Object... params) {
        try (Connection conn = DatabaseSetService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении: " + sql, e);
        }
    }

    /**
     * Удаление записи
     */
    public static int delete(String sql, Object... params) {
        return update(sql, params); // delete это тот же update по сути
    }

    /**
     * Пакетная вставка нескольких записей
     */
    public static int[] batchInsert(String sql, List<Object[]> batchParams) {
        try (Connection conn = DatabaseSetService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Object[] params : batchParams) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                ps.addBatch();
            }

            return ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при пакетной вставке: " + sql, e);
        }
    }

    /**
     * Выполнение запроса в рамках транзакции
     */
    public static <T> T executeInTransaction(TransactionCallback<T> callback) {
        Connection conn = null;
        try {
            conn = DatabaseSetService.getConnection();
            conn.setAutoCommit(false);

            T result = callback.execute(conn);

            conn.commit();
            return result;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Ошибка при откате транзакции", ex);
                }
            }
            throw new RuntimeException("Ошибка в транзакции", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Ошибка при закрытии соединения", e);
                }
            }
        }
    }

    /**
     * Функциональный интерфейс для транзакций
     */
    @FunctionalInterface
    public interface TransactionCallback<T> {
        T execute(Connection conn) throws Exception;
    }
}