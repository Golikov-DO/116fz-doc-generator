package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.service.ObjectService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ImageBlockFactory {

    private final ObjectService objectService;

    public ImageBlockFactory(ObjectService objectService) {
        this.objectService = objectService;
    }

    public Map<String,Object> build(DocumentSet documentSet) throws SQLException {

        Map<String,Object> data = new HashMap<>();

        ObjectModel obj =
                objectService.getByOrgId(documentSet.orgId());

        byte[] blob = obj.planAndDiagram();

        if (blob != null && blob.length > 0) {
            data.put("OBJ_SCHEME_IMAGE", blob);
        }
        //data.put("OBJ_SHEME_ EQUIPMENT_IMAGE", null);

        return data;
    }
}