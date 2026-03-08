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

    private final ChildService<ObjectCompositionKchs> objectCompositionKchsService;
    private final ChildService<ObjectModel> objectService;
    private final ChildService<ObjectTechnologicalEquipment> objectTechnologicalEquipmentService;
    private final ChildService<ObjectAccidentScenarios> objectAccidentScenariosService;
    private final ChildService<ObjectMainScenarios> objectMainScenariosService;
    private final ChildService<ObjectFireEquipment> objectFireEquipmentService;
    private final ChildService<ObjectPersonsResponsible> objectPersonsResponsibleService;

    public TableBlockFactory(
            ChildService<ObjectCompositionKchs> objectCompositionKchsService,
            ChildService<ObjectModel> objectService,
            ChildService<ObjectTechnologicalEquipment> objectTechnologicalEquipmentService,
            ChildService<ObjectAccidentScenarios> objectAccidentScenariosService,
            ChildService<ObjectMainScenarios> objectMainScenariosService,
            ChildService<ObjectFireEquipment> objectFireEquipmentService,
            ChildService<ObjectPersonsResponsible> objectPersonsResponsibleService) {
        this.objectCompositionKchsService = objectCompositionKchsService;
        this.objectService = objectService;
        this.objectTechnologicalEquipmentService = objectTechnologicalEquipmentService;
        this.objectAccidentScenariosService = objectAccidentScenariosService;
        this.objectMainScenariosService = objectMainScenariosService;
        this.objectFireEquipmentService = objectFireEquipmentService;
        this.objectPersonsResponsibleService = objectPersonsResponsibleService;
    }

    public Map<String, Object> build(int objectId) throws SQLException {
        Map<String, Object> data = new HashMap<>();

        ObjectModel obj = objectService.getOneByParentId(objectId);

        for (int i = 1; i <= 9; i++) {
            if (i == 2 || i == 6) continue;

            String placeholderKey = "OBJ_TABLE_" + i + "_PLACEHOLDER";

            // Получаем данные для конкретного индекса.
            switch (i){
                case 1 -> fillTable(data, placeholderKey, objectTechnologicalEquipmentService.getManyByParentId(obj.getId()),
                        equipment -> new String[]{String.valueOf(equipment.getNum()), equipment.getName(), equipment.getCharacteristics()});
                case 3 -> fillTable(data, placeholderKey, objectAccidentScenariosService.getManyByParentId(obj.getId()),
                        accidentScenarios -> new String[]{accidentScenarios.getScenarios(), accidentScenarios.getScheme()});
                case 4 -> fillTable(data, placeholderKey, objectMainScenariosService.getManyByParentId(obj.getId()),
                        mainScenarios -> new String[]{mainScenarios.getEquipmentName(), mainScenarios.getEvent(), mainScenarios.getScenariosList()});
                case 5 -> fillTable(data, placeholderKey, objectFireEquipmentService.getManyByParentId(obj.getId()),
                        equipment -> new String[]{String.valueOf(equipment.getNumber()), equipment.getProductName(), equipment.getQuantity(), equipment.getLocation()});
                case 7 -> fillTable(data, placeholderKey, objectPersonsResponsibleService.getManyByParentId(obj.getId()),
                        personsResponsible -> new String[]{String.valueOf(personsResponsible.getNumber()), personsResponsible.getFullName(), personsResponsible.getPosition()});
                case 8 -> fillTable(data, placeholderKey, objectCompositionKchsService.getManyByParentId(obj.getId()),
                        kchs -> new String[]{String.valueOf(kchs.getNumber()), kchs.getPosition(), kchs.getFullName(), kchs.getWorkPhone(), kchs.getCellPhone(), kchs.getHomeAddress()
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