package com.caseo.domain.repository;

import com.caseo.domain.model.ReferenceTableTitle;
import java.sql.SQLException;
import java.util.List;

public interface ReferenceTableTitleRepository {
    List<ReferenceTableTitle> findAll() throws SQLException;
}
