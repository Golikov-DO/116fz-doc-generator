package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectOrderMinimumBalance;
import com.caseo.domain.repository.ObjectOrderMinimumBalanceRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcObjectOrderMinimumBalanceRepository extends BaseJdbcRepository<ObjectOrderMinimumBalance> implements ObjectOrderMinimumBalanceRepository {

    @Override
    protected String table() {
        return "object_order_minimum_balance";
    }

    @Override
    protected RowMapper<ObjectOrderMinimumBalance> mapper() {
        return rs -> new ObjectOrderMinimumBalance(
                rs.getInt("object_id"),
                rs.getInt("number"),
                rs.getString("date")
        );
    }

    @Override
    public ObjectOrderMinimumBalance findByObjectId(int objectId) {
        return findOne("object_id = ?", objectId).orElse(null);
    }

    @Override
    public void save(ObjectOrderMinimumBalance objectOrderMinimumBalance, int objectId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("number", objectOrderMinimumBalance.number());
        String date = objectOrderMinimumBalance.date();
        if (date == null || date.trim().isEmpty()) {
            data.put("date", null);
        } else {
            // Чтобы избежать ошибки "expression is of type character varying",
            // лучше передать объект LocalDate, а не строку
            data.put("date", java.time.LocalDate.parse(date));
        }

        insert(data);
    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}
