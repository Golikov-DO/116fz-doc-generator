package com.caseo.word.layout;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactTableLayoutService {
    private final ObjectService objectService;
    private final EmergencyServicesService emergencyService;
    private final RegionAuthoritiesService regionalService;
    private final OrganizationContactService organizationContactService;
    private final OrganizationService organizationService;

    public ContactTableLayoutService(ObjectService objectService,
                                     EmergencyServicesService emergencyService,
                                     RegionAuthoritiesService regionalService,
                                     OrganizationContactService organizationContactService,
                                     OrganizationService organizationService) {
        this.objectService = objectService;
        this.emergencyService = emergencyService;
        this.regionalService = regionalService;
        this.organizationContactService = organizationContactService;
        this.organizationService = organizationService;
    }

    public List<String[]> getContactTableData(DocumentSet documentSet) throws SQLException {
        ObjectModel obj = objectService.getByOrgId(documentSet.orgId());
        List<String[]> tableRows = new ArrayList<>();
        int counter = 1;

        // --- Секция 1: Emergency (1-5) ---
        for (EmergencyServices es : emergencyService.getAll()) {
            tableRows.add(new String[]{String.valueOf(counter++), es.serviceName(), es.positionContact(), es.phone(), es.address()});
        }

        // --- Секция 2: Regional (6-9) ---
        List<RegionalAuthorities> regionalList = regionalService.getByObjectId(obj.id());
        for (RegionalAuthorities regionalAuthorities : regionalList) {
            String numStr;

            // ВАЖНО: Условие по индексу в списке (0-3 для 6,7,8,9)
            // Чтобы 8 и 9 визуально стали "одной восьмеркой"
            if (counter == 8) {
                numStr = "V_MERGE_START:8";
                counter++; // Прыгаем на 9, но для следующей строки сделаем проверку
            } else if (counter == 9) {
                numStr = "V_MERGE_CONT";
                // counter НЕ увеличиваем, чтобы следующая строка (после разделителя) стала 9-й
            } else {
                numStr = String.valueOf(counter++);
            }

            tableRows.add(new String[]{numStr, regionalAuthorities.name(), regionalAuthorities.department(), regionalAuthorities.phone_number(), regionalAuthorities.address()});
        }

        // --- Секция 3: Разделитель (БЕЗ СЧЕТЧИКА) ---
        var org = organizationService.getById(documentSet.orgId());
        tableRows.add(new String[]{"H_MERGE_FULL", org.organizationShortName(), "", "", ""});

        // --- Секция 4: Organization Contact (Начнется с 9) ---
        for (OrganizationContact oc : organizationContactService.getByOrganizationId(documentSet.orgId())) {
            tableRows.add(new String[]{String.valueOf(counter++), oc.fullName(), oc.position(), oc.phones(), oc.address()});
        }

        return tableRows;
    }
}