package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.repository.ObjectAddressRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcObjectAddressRepository extends BaseJdbcRepository<ObjectAddress> implements ObjectAddressRepository {

    @Override
    protected String table() {
        return "object_address";
    }

    @Override
    protected RowMapper<ObjectAddress> mapper() {
        return rs -> new ObjectAddress(
                rs.getInt("object_id"),
                rs.getObject("index") != null ? rs.getInt("index") : 0, // Безопасное получение int
                rs.getString("constituent_entity"),
                rs.getString("area_hierarchy"),
                rs.getString("city_name"),
                rs.getString("street"),
                rs.getString("house"),
                rs.getString("coordinates"),
                rs.getString("raw_address")
        );
    }

    @Override
    public ObjectAddress findByObjectId(int objectId) {
        return findOne("object_id = ?", objectId).orElse(null);
    }

    @Override
    public void save(ObjectAddress objectAddress, int objectId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("index", objectAddress.index());
        data.put("constituent_entity", objectAddress.constituentEntity());
        data.put("area_hierarchy", objectAddress.areaHierarchy());
        data.put("city_name", objectAddress.city());
        data.put("street", objectAddress.street());
        data.put("house", objectAddress.house());
        data.put("coordinates", objectAddress.coordinates());
        data.put("raw_address", objectAddress.rawAddress());

        insert(data);
    }

    public void deleteByObjectId(int objectId) {
        delete("object_id = ?", objectId);
    }
}
