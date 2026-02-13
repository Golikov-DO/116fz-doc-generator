package com.caseo.word.factory;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TableBlockFactory {

    private final AccidentScenariosService accidentScenariosService;
    private final CompositionKchsService compositionKchsService;
    private final FireEquipmentService fireEquipmentService;
    private final MainScenariosService mainScenariosService;
    private final ObjectService objectService;
    private final OrganizationService organizationService;
    private final PersonsResponsibleService personsResponsibleService;
    private final TechnologicalEquipmentService technologicalEquipmentService;

    public TableBlockFactory(
            OrganizationService organizationService,
            ObjectService objectService,
            TechnologicalEquipmentService technologicalEquipmentService,
            AccidentScenariosService accidentScenariosService,
            MainScenariosService mainScenariosService,
            FireEquipmentService fireEquipmentService,
            PersonsResponsibleService personsResponsibleService,
            CompositionKchsService compositionKchsService
    ) {
        this.organizationService = organizationService;
        this.objectService = objectService;
        this.technologicalEquipmentService = technologicalEquipmentService;
        this.accidentScenariosService = accidentScenariosService;
        this.mainScenariosService = mainScenariosService;
        this.fireEquipmentService = fireEquipmentService;
        this.personsResponsibleService = personsResponsibleService;
        this.compositionKchsService = compositionKchsService;
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {
        Map<String, Object> data = new HashMap<>();

        Organization org = organizationService.getById(documentSet.orgId());
        ObjectModel obj = objectService.getByOrgId(org.organizationId());

        for (int i = 1; i <= 9; i++) {
            if (i == 2 || i == 6) continue;

            String placeholderKey = "OBJ_TABLE_" + i + "_PLACEHOLDER";

            // Получаем данные для конкретного индекса.
            switch (i){
                case 1 -> fillTable(data, placeholderKey, technologicalEquipmentService.getByObjectId(obj.id()),
                        r -> new String[]{String.valueOf(r.num()), r.name(), r.characteristics()});
                case 3 -> fillTable(data, placeholderKey, accidentScenariosService.getByObjectId(obj.id()),
                        r -> new String[]{r.scenarios(), r.scheme()});
                case 4 -> fillTable(data, placeholderKey, mainScenariosService.getByObjectId(obj.id()),
                        r -> new String[]{r.equipmentName(), r.event(), r.scenariosList()});
                case 5 -> fillTable(data, placeholderKey, fireEquipmentService.getByObjectId(obj.id()),
                        r -> new String[]{String.valueOf(r.number()), r.productName(), r.quantity(), r.location()});
                case 7 -> fillTable(data, placeholderKey, personsResponsibleService.getByObjectId(obj.id()),
                        r -> new String[]{String.valueOf(r.number()), r.fullName(), r.position()});
                case 8 -> fillTable(data, placeholderKey, compositionKchsService.getByObjectId(obj.id()),
                        r -> new String[]{
                                String.valueOf(r.number()),
                                r.position(),
                                r.fullName(),
                                r.workPhone(),
                                r.cellPhone(),
                                r.homeAddress()
                });
                default -> data.put(placeholderKey, null);
            }
        }

        return data;
    }

    private <T> void fillTable(Map<String, Object> data, String key, List<T> rows, Function<T, String[]> mapper) {
        if (rows != null && !rows.isEmpty()) {
            data.put(key, rows.stream().map(mapper).collect(Collectors.toList()));
        } else {
            data.put(key, null);
        }
    }
}