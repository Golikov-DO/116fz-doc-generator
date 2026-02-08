package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.model.TechnologicalEquipment;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.OrganizationService;
import com.caseo.domain.service.TechnologicalEquipmentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBlockFactory {

    private final OrganizationService organizationService;
    private final ObjectService objectService;
    private final TechnologicalEquipmentService technologicalEquipmentService;

    public TableBlockFactory(
            OrganizationService organizationService,
            ObjectService objectService,
            TechnologicalEquipmentService technologicalEquipmentService
    ) {
        this.organizationService = organizationService;
        this.objectService = objectService;
        this.technologicalEquipmentService = technologicalEquipmentService;
    }

    public Map<String,Object> build(DocumentSet documentSet) throws SQLException {

        Map<String,Object> data = new HashMap<>();

        Organization org = organizationService.getById(documentSet.orgId());

        ObjectModel obj = objectService.getByOrgId(org.organizationId());

        List<TechnologicalEquipment> rows = technologicalEquipmentService.getByObject(obj.id());

        if (rows == null || rows.isEmpty()) {
            return data;
        }

        List<String[]> tableRows = new ArrayList<>();

        for (TechnologicalEquipment r : rows) {
            tableRows.add(new String[]{
                    r.num()+"",
                    r.name(),
                    r.characteristics()
            });
        }

        data.put("TECHNOLOGICAL_EQUIPMENT_TABLE", tableRows);
       // data.put("OBJ_GOCHS_TABLE", null);

        return data;
    }
}