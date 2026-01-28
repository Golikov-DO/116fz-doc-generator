package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.model.Table1;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.OrganizationService;
import com.caseo.domain.service.Table1Service;
import com.caseo.word.blocks.table.TableBlock;
import com.caseo.word.blocks.table.TableColumn;
import com.caseo.word.blocks.table.TableRow;
import com.caseo.word.blocks.table.TableSchema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleTableBlockFactory {

    private final OrganizationService organizationService;
    private final ObjectService objectService;
    private final Table1Service table1Service;

    public SimpleTableBlockFactory(
            OrganizationService organizationService,
            ObjectService objectService,
            Table1Service table1Service
    ) {
        this.organizationService = organizationService;
        this.objectService = objectService;
        this.table1Service = table1Service;
    }

    /**
     * Аналог старого Table1Block
     */
    public TableBlock build(DocumentSet documentSet) throws SQLException {

        // 1. Получаем организацию
        Organization org =
                organizationService.getById(documentSet.getOrgId());

        // 2. Получаем объект
        ObjectModel obj =
                objectService.getByOrgId(org.getOrganizationId());

        // 3. Получаем строки
        List<Table1> rows =
                table1Service.getByObject(obj.getId());

        if (rows == null || rows.isEmpty()) {
            return null; // таблицы нет → блок не создаём
        }

        // 4. Schema таблицы
        TableSchema schema = new TableSchema(List.of(
                new TableColumn("NUM", "№", "NUMBER"),
                new TableColumn("NAME", "Наименование", "TEXT"),
                new TableColumn("CHAR", "Характеристики", "TEXT")
        ));

        // 5. Rows
        List<TableRow> tableRows = new ArrayList<>();

        for (Table1 r : rows) {
            tableRows.add(new TableRow(Map.of(
                    "NUM", r.getNum(),
                    "NAME", r.getName(),
                    "CHAR", r.getCharacteristics()
            )));
        }

        // 6. Block
        return new TableBlock("TABLE_1", schema, tableRows);
    }
}