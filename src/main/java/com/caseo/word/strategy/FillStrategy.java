package com.caseo.word.strategy;

import com.caseo.domain.model.DocumentSet;

import java.sql.SQLException;
import java.util.Map;

public interface FillStrategy {
    Map<String, Object> build(DocumentSet documentSet) throws SQLException;
}
