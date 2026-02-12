package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.model.TechnologicalEquipment;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.OrganizationService;
import com.caseo.domain.service.TechnologicalEquipmentService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {
        Map<String, Object> data = new HashMap<>();

        Organization org = organizationService.getById(documentSet.orgId());
        ObjectModel obj = objectService.getByOrgId(org.organizationId());

        for (int i = 1; i <= 9; i++) {
            if (i == 2) continue;

            String placeholderKey = "OBJ_TABLE_" + i + "_PLACEHOLDER";

            // Получаем данные для конкретного индекса.
            List<TechnologicalEquipment> rows = getRowsByIndex(i, obj.id());

            if (rows != null && !rows.isEmpty()) {
                // Если данные есть — трансформируем в список String[]
                List<String[]> tableRows = rows.stream()
                        .map(r -> new String[]{
                                String.valueOf(r.num()),
                                r.name(),
                                r.characteristics()
                        })
                        .collect(Collectors.toList());

                data.put(placeholderKey, tableRows);
            } else {
                // Если данных нет — кладем null
                // Это заставит Docx4jTableBlockRenderer удалить таблицу из Word
                data.put(placeholderKey, null);
            }
        }

        return data;
    }

    private List<TechnologicalEquipment> getRowsByIndex(int index, int objectId) throws SQLException {
        return switch (index) {
            case 1 -> technologicalEquipmentService.getByObject(objectId);
            // Когда добавлю другие таблицы, просто раскомментируй и впиши сервис:
            // case 3 -> otherEquipmentService.getByObject(objectId);
            // case 4 -> vesselsService.getByObject(objectId);
            default -> List.of(); // Для всех нереализованных таблиц — пустой список (удаление)
        };
    }
}