package com.caseo.word.tag;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.model.Table1;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.OrganizationService;
import com.caseo.domain.service.Table1Service;

import java.sql.SQLException;
import java.util.*;

public class Table1TagDataBuilder {

    private final OrganizationService organizationService;
    private final ObjectService objectService;
    private final Table1Service table1Service;

    public Table1TagDataBuilder(
            OrganizationService organizationService,
            ObjectService objectService,
            Table1Service table1Service
    ) {
        this.organizationService = organizationService;
        this.objectService = objectService;
        this.table1Service = table1Service;
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {

        Map<String, Object> data = new HashMap<>();

        Organization org =
                organizationService.getById(documentSet.getOrgId());

        ObjectModel obj =
                objectService.getByOrgId(org.getOrganizationId());

        List<Table1> rows =
                table1Service.getByObject(obj.getId());

        if (rows == null || rows.isEmpty()) {
            return data;
        }

        List<String[]> tableRows = new ArrayList<>();

        for (Table1 r : rows) {
            tableRows.add(new String[]{
                    r.getNum()+"",
                    r.getName(),
                    r.getCharacteristics()
            });
        }

        // ЖЁСТКАЯ ПРИВЯЗКА К ТЭГУ
        data.put("TABLE_1", tableRows);

        return data;
    }
}