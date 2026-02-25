package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.AsfSignerRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcAsfSignerRepository extends BaseJdbcRepository<AsfSigner> implements AsfSignerRepository {

    @Override
    protected String table() {
        return "asf_signer";
    }

    @Override
    protected RowMapper<AsfSigner> mapper() {
        return rs -> new AsfSigner(
                rs.getInt("id"),
                rs.getInt("asf_id"),
                rs.getString("signer_name"),
                rs.getString("signer_position")
        );
    }

    @Override
    public AsfSigner findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }

    @Override
    public List<AsfSigner> getByAsfId(int asfId) throws SQLException {
        return findList("asf_id = ?", asfId);
    }

    @Override
    public void save(AsfSigner asfSigner, int asfId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", asfId);
        data.put("asf_id", asfId);
        data.put("signer_name", asfSigner.name());
        data.put("signer_position", asfSigner.position());

        insert(data);
    }

    @Override
    public void deleteByAsfId(int asfId) throws SQLException {
        delete("asf_id = ?", asfId);
    }

    @Override
    public AsfSigner getById(int id) throws SQLException {
        return findOne("id = ?", id).orElse(null);
    }
}
