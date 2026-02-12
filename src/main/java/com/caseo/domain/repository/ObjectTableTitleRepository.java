package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectTableTitle;
import java.sql.SQLException;
import java.util.List;

public interface ObjectTableTitleRepository {
    List<ObjectTableTitle> findAll() throws SQLException;
}
