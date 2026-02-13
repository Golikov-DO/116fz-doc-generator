package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectOrderMinimumBalance;
import com.caseo.domain.repository.ObjectOrderMinimumBalanceRepository;

public class JdbcObjectOrderMinimumBalanceRepository extends BaseJdbcRepository<ObjectOrderMinimumBalance> implements ObjectOrderMinimumBalanceRepository {

    @Override
    protected String table() {
        return "object_order_minimum_balance";
    }

    @Override
    protected RowMapper<ObjectOrderMinimumBalance> mapper() {
        return rs -> new ObjectOrderMinimumBalance(
                rs.getInt("number"),
                rs.getString("date")
        );
    }

    @Override
    public ObjectOrderMinimumBalance findByObjectId(int objectId) {
        return findOne("obj_id = ?", objectId).orElse(null);
    }
}
