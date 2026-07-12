package ru.ecospas.word.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.repository.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TableBlockFactory {

    private final ObjectCompositionKchsRepository objectCompositionKchsRepository;
    private final ObjectModelRepository objectRepository;
    private final ObjectTechnologicalEquipmentRepository objectTechnologicalEquipmentRepository;
    private final ObjectFireEquipmentRepository objectFireEquipmentRepository;
    private final ObjectPersonsResponsibleRepository objectPersonsResponsibleRepository;

    public Map<String, Object> build(int objectId) throws SQLException {
        Map<String, Object> data = new HashMap<>();

        ObjectModel obj = objectRepository.findById(objectId).orElseThrow();

        for (int i = 1; i <= 9; i++) {
            if (i == 2 || i == 3 || i == 4 || i == 6) continue;

            String placeholderKey = "OBJ_TABLE_" + i + "_PLACEHOLDER";

            // Get data for a specific index.
            switch (i) {
                case 1 -> fillTable(data, placeholderKey,
                        objectTechnologicalEquipmentRepository.findAllByObjectId(obj.getId()),
                        equipment ->
                                new String[]{String.valueOf(equipment.getNum()), equipment.getName(),
                                        equipment.getCharacteristics()});
                case 5 -> fillTable(data, placeholderKey,
                        objectFireEquipmentRepository.findAllByObjectIdOrderByNumber(obj.getId()),
                        equipment ->
                                new String[]{String.valueOf(equipment.getNumber()),
                                        equipment.getProductName(), equipment.getQuantity(), equipment.getLocation()});
                case 7 -> fillTable(data, placeholderKey,
                        objectPersonsResponsibleRepository.findAllByObjectIdOrderByNumber(obj.getId()),
                        personsResponsible ->
                                new String[]{String.valueOf(personsResponsible.getNumber()),
                                        personsResponsible.getFullName(),
                                        personsResponsible.getPosition()});
                case 8 -> fillTable(data, placeholderKey,
                        objectCompositionKchsRepository.findAllByObjectId(obj.getId()),
                        kchs ->
                                new String[]{String.valueOf(kchs.getNumber()),
                                        kchs.getPosition(), kchs.getFullName(),
                                        kchs.getWorkPhone(), kchs.getCellPhone(), kchs.getHomeAddress()
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