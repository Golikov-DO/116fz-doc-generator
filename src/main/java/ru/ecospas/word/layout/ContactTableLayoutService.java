package ru.ecospas.word.layout;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactTableLayoutService {

    private final OrganizationRepository organizationRepository;
    private final ObjectModelRepository objectRepository;
    private final ReferenceEmergencyServicesRepository emergencyRepository;
    private final ObjectRegionalAuthoritiesRepository regionalRepository;
    private final OrganizationContactRepository organizationContactRepository;

    public List<String[]> getContactTableData(int orgId, int objectId) {
        ObjectModel obj = objectRepository.findById(objectId).orElseThrow();
        List<String[]> tableRows = new ArrayList<>();
        int counter = 1;

        // --- Section 1: Emergency (1-5) ---
        for (ReferenceEmergencyServices es : emergencyRepository.findAll()) {
            tableRows.add(new String[]{
                    String.valueOf(counter++),
                    es.getServiceName(),
                    es.getPositionContact(),
                    es.getPhone(),
                    es.getAddress()});
        }

        // --- Section 2: Regional (6-9) ---
        List<ObjectRegionalAuthorities> regionalList = regionalRepository
                .findAllByObjectCityId(obj.getCity().getId());
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

            tableRows.add(new String[]{
                    numStr, objectRegionalAuthorities.getName(),
                    objectRegionalAuthorities.getDepartment(),
                    objectRegionalAuthorities.getPhoneNumber(),
                    objectRegionalAuthorities.getAddress()
            });
        }

        // --- Section 3: Separator (WITHOUT COUNTER) ---
        Organization org = organizationRepository.findById(orgId).orElseThrow();
        tableRows.add(new String[]{"H_MERGE_FULL", org.getOrganizationShortName(), "", "", ""});

        // --- Section 4: Organization Contact (start with 9) ---
        for (OrganizationContact organizationContact : organizationContactRepository
                .findAllByOrganizationId(orgId)) {
            tableRows.add(
                    new String[]{
                            String.valueOf(counter++),
                            organizationContact.getFullName(),
                            organizationContact.getPosition(),
                            organizationContact.getPhones(),
                            organizationContact.getAddress()
                    }
            );
        }

        return tableRows;
    }
}