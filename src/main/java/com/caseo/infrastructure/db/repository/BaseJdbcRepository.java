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
}