package ru.ecospas.word.layout;

import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.ArrayList;
import java.util.List;

public class ContactTableLayoutService {
    private final ParentService<ObjectModel> objectService;
    private final ParentService<ReferenceEmergencyServices> emergencyService;
    private final ChildService<ObjectRegionalAuthorities> regionalService;
    private final ChildService<OrganizationContact> organizationContactService;
    private final ParentService<Organization> organizationService;

    public ContactTableLayoutService(
            ParentService<ObjectModel> objectService,
            ParentService<ReferenceEmergencyServices> emergencyService,
            ChildService<ObjectRegionalAuthorities> regionalService,
            ChildService<OrganizationContact> organizationContactService,
            ParentService<Organization> organizationService) {
        this.objectService = objectService;
        this.emergencyService = emergencyService;
        this.regionalService = regionalService;
        this.organizationContactService = organizationContactService;
        this.organizationService = organizationService;
    }

    public List<String[]> getContactTableData(int orgId, int objectId) {
        ObjectModel obj = objectService.getOneById(objectId);
        List<String[]> tableRows = new ArrayList<>();
        int counter = 1;

        // --- Секция 1: Emergency (1-5) ---
        for (ReferenceEmergencyServices es : emergencyService.getMany()) {
            tableRows.add(new String[]{
                    String.valueOf(counter++), es.getServiceName(), es.getPositionContact(), es.getPhone(), es.getAddress()});
        }

        // --- Секция 2: Regional (6-9) ---
        ReferenceCity city = obj.getCity();
        List<ObjectRegionalAuthorities> regionalList = regionalService.getManyByParentId(city.getId());
        for (ObjectRegionalAuthorities objectRegionalAuthorities : regionalList) {
            String numStr;
            if (counter == 8) {
                numStr = "V_MERGE_START:8";
                counter++;
            } else if (counter == 9) {
                numStr = "V_MERGE_CONT";
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