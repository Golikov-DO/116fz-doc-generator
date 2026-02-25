package com.caseo.infrastructure.db.repository;

import com.caseo.infrastructure.db.DbUtils;

import java.util.*;

public abstract class BaseJdbcRepository<T> {
    protected abstract String table();

    protected abstract RowMapper<T> mapper();

    // Запрсопостроитель
    // Универсальный сборщик запросов (Мастер-метод)
    protected String buildSql(String select, String join, String where, String order, String limit) {
        StringBuilder sql = new StringBuilder(select != null ? select : "SELECT " + table() + ".*");
        sql.append(" FROM ").append(table());

        if (join != null && !join.isEmpty()) sql.append(" ").append(join);
        if (where != null && !where.isEmpty()) sql.append(" WHERE ").append(where);
        if (order != null && !order.isEmpty()) sql.append(" ").append(order);
        if (limit != null && !limit.isEmpty()) sql.append(" ").append(limit);


        return sql.toString();
    }

    // --- МЕТОДЫ ДЛЯ ОДНОГО ОБЪЕКТА (findOne) ---

    // поиск по ID с LIMIT 1
    protected Optional<T> findOne(String where, Object... params) {
        String sql = buildSql(null, null, where, null, "LIMIT 1");
        return Optional.ofNullable(DbUtils.queryOne(sql, mapper(), params));
    }

    // поиск по ID с кастомным FROM и с LIMIT 1
    protected Optional<T> findOne(String join, String where, Object... params) {
        String sql = buildSql(null, join, where, null, "LIMIT 1");
        return Optional.ofNullable(DbUtils.queryOne(sql, mapper(), params));
    }

    // поиск по ID БЕЗ лимита в виде списка
    protected List<T> findList(String where, Object... params) {
        String sql = buildSql(null, null, where, null, null);
        return DbUtils.queryMany(sql, mapper(), params);
    }

    // поиск по ID с ORDER BY для списка
    protected List<T> findList(String where, String order, Object... params) {
        String sql = buildSql(null, null, where, order, null);
        return DbUtils.queryMany(sql, mapper(), params);
    }

    // кастомный запрос для списка
    protected <R> R queryCustom(String sql, RowMapper<R> customMapper, Object... params) {
        return DbUtils.queryOne(sql, customMapper, params);
    }

    // запрос прям в коде для списка
    protected List<T> queryMany(String sql, Object... params) {
        return DbUtils.queryMany(sql, mapper(), params);
    }

// ========== НОВЫЕ МЕТОДЫ ДЛЯ ЗАПИСИ ==========

    /**
     * Вставка записи
     * @param fields имена полей
     * @param values значения
     * @return сгенерированный ID
     */
    protected int insert(String[] fields, Object... values) {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Количество полей не совпадает с количеством значений");
        }

        String sql = buildInsertSql(fields);
        return DbUtils.insert(sql, values);
    }

    /**
     * Вставка записи из Map
     */
    protected int insert(Map<String, Object> fieldValues) {
        String[] fields = fieldValues.keySet().toArray(new String[0]);
        Object[] values = fieldValues.values().toArray();
        return insert(fields, values);
    }

    /**
     * Обновление записи
     */
    protected int update(String[] fields, Object[] values, String where, Object... whereParams) {
        String sql = buildUpdateSql(fields, where);

        // Объединяем значения
        Object[] allParams = new Object[values.length + whereParams.length];
        System.arraycopy(values, 0, allParams, 0, values.length);
        System.arraycopy(whereParams, 0, allParams, values.length, whereParams.length);

        return DbUtils.update(sql, allParams);
    }

    /**
     * Удаление записи
     */
    protected int delete(String where, Object... params) {
        String sql = "DELETE FROM " + table() + " WHERE " + where;
        return DbUtils.delete(sql, params);
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private String buildInsertSql(String[] fields) {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(table()).append(" (");

        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sql.append(", ");
            sql.append(fields[i]);
        }

        sql.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sql.append(", ");
            sql.append("?");
        }
        sql.append(")");

        return sql.toString();
    }

    private String buildUpdateSql(String[] fields, String where) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(table()).append(" SET ");

        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sql.append(", ");
            sql.append(fields[i]).append(" = ?");
        }

        if (where != null && !where.isEmpty()) {
            sql.append(" WHERE ").append(where);
        }

        return sql.toString();
    }
}