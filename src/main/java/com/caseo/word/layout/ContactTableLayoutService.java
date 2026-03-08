package com.caseo.word.layout;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;

import java.util.ArrayList;
import java.util.List;

public class ContactTableLayoutService {
    private final ChildService<ObjectModel> objectService;
    private final ParentService<ReferenceEmergencyServices> emergencyService;
    private final ChildService<ObjectRegionalAuthorities> regionalService;
    private final ChildService<OrganizationContact> organizationContactService;  // ИЗМЕНЕНО!
    private final ParentService<Organization> organizationService;

    public ContactTableLayoutService(
            ChildService<ObjectModel> objectService,
            ParentService<ReferenceEmergencyServices> emergencyService,
            ChildService<ObjectRegionalAuthorities> regionalService,
            ChildService<OrganizationContact> organizationContactService,  // ИЗМЕНЕНО!
            ParentService<Organization> organizationService) {
        this.objectService = objectService;
        this.emergencyService = emergencyService;
        this.regionalService = regionalService;
        this.organizationContactService = organizationContactService;
        this.organizationService = organizationService;
    }

    public List<String[]> getContactTableData(int orgId, int objectId) {
        ObjectModel obj = objectService.getOneByParentId(objectId);
        List<String[]> tableRows = new ArrayList<>();
        int counter = 1;

        // --- Секция 1: Emergency (1-5) ---
        for (ReferenceEmergencyServices es : emergencyService.getMany()) {
            tableRows.add(new String[]{String.valueOf(counter++), es.getServiceName(), es.getPositionContact(), es.getPhone(), es.getAddress()});
        }

        // --- Секция 2: Regional (6-9) ---
        List<ObjectRegionalAuthorities> regionalList = regionalService.getManyByParentId(obj.getId());
        for (ObjectRegionalAuthorities objectRegionalAuthorities : regionalList) {
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

            tableRows.add(new String[]{numStr, objectRegionalAuthorities.getName(), objectRegionalAuthorities.getDepartment(), objectRegionalAuthorities.getPhoneNumber(), objectRegionalAuthorities.getAddress()});
        }

        // --- Секция 3: Разделитель (БЕЗ СЧЕТЧИКА) ---
        var org = organizationService.getOneById(orgId);
        tableRows.add(new String[]{"H_MERGE_FULL", org.getOrganizationShortName(), "", "", ""});

        // --- Секция 4: Organization Contact (Начнется с 9) ---
        for (OrganizationContact organizationContact : organizationContactService.getManyByParentId(orgId)) {
            tableRows.add(new String[]{String.valueOf(counter++), organizationContact.getFullName(), organizationContact.getPosition(), organizationContact.getPhones(), organizationContact.getAddress()});
        }

        return tableRows;
    }
}