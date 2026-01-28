package com.caseo.word.tag;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.service.ObjectService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ObjectSchemeImageTagBuilder {

    private final ObjectService objectService;

    public ObjectSchemeImageTagBuilder(ObjectService objectService) {
        this.objectService = objectService;
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {

        ObjectModel obj =
                objectService.getByOrgId(documentSet.getOrgId());

        byte[] blob = obj.getPlanAndDiagram(); // РЕАЛЬНОЕ ПОЛЕ

        Map<String, Object> map = new HashMap<>();

        if (blob != null && blob.length > 0) {
            map.put("OBJECT_SCHEME", blob);
        }

        return map;
    }
}