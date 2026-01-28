package com.caseo.word.blocks.text;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.domain.util.*;
import com.caseo.word.ListFormat;
import com.caseo.word.WordListBuilder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextPlaceholderService {

    private final OrganizationService organizationService;
    private final OrgSignerService orgSignerService;
    private final ObjectService objectService;
    private final ObjectStructureService objectStructureService;
    private final ObjectAddressService objectAddressService;
    private final ObjectCityService objectCityService;
    private final TechnologicalBlockService technologicalBlockService;
    private final HazardousSubstanceService hazardousSubstanceService;
    private final AsfService asfService;
    private final AsfSignerService asfSignerService;

    public TextPlaceholderService(
            OrganizationService organizationService,
            OrgSignerService orgSignerService,
            ObjectService objectService,
            ObjectStructureService objectStructureService,
            ObjectAddressService objectAddressService,
            ObjectCityService objectCityService,
            TechnologicalBlockService technologicalBlockService,
            HazardousSubstanceService hazardousSubstanceService,
            AsfService asfService,
            AsfSignerService asfSignerService
    ) {
        this.organizationService = organizationService;
        this.orgSignerService = orgSignerService;
        this.objectService = objectService;
        this.objectStructureService = objectStructureService;
        this.objectAddressService = objectAddressService;
        this.objectCityService = objectCityService;
        this.technologicalBlockService = technologicalBlockService;
        this.hazardousSubstanceService = hazardousSubstanceService;
        this.asfService = asfService;
        this.asfSignerService = asfSignerService;
    }

    public Map<String, String> build(DocumentSet d) throws SQLException {

        Map<String, String> map = new HashMap<>();

        Organization org = organizationService.getById(d.getOrgId());
        OrgSigner orgSigner = orgSignerService.getByOrganizationId(org.getOrganizationId());
        ObjectModel objectModel = objectService.getByOrgId(org.getOrganizationId());

        List<ObjectStructureP1> objectStructureP1 =
                objectStructureService.getByObject(objectModel.getId());

        List<TechnologicalBlock> technologicalBlock =
                technologicalBlockService.getByObject(objectModel.getId());

        HazardousSubstance hazardousSubstance =
                hazardousSubstanceService.getById(objectModel.getId());

        Asf asf = asfService.getByDocumentSet(d.getId());
        AsfSigner asfSigner = asfSignerService.getByAsfId(asf.getId());

        map.put("ORG_NAME", org.getOrganizationName());
        map.put("ORG_SHORT", org.getOrganizationShortName());
        map.put("ORG_ADDRESS", org.getOrganizationAddress());
        map.put("ORG_SIGNER_POSITION", orgSigner.getPosition());
        map.put("ORG_SIGNER_NAME", orgSigner.getName());

        map.put("ASF_SIGNER_POSITION", asfSigner.getPosition());
        map.put("ASF_SHORT", asf.getShortName());
        map.put("ASF_SIGNER_NAME", asfSigner.getName());

// ---------- ASF TEXT BLOCK ----------

        map.put("ASF_FULL_NAME", asf.getFullName());
        map.put("ASF_STATUS", asf.getStatus());
        map.put("ASF_HEADER", AsfHeaderBuilder.build(asf));

        map.put(
                "ASF_CERTIFICATE_TEXT",
                AsfCertificateTextBuilder.build(asf.getCertificate())
        );

        map.put(
                "ASF_WORK_TYPES",
                asf.getWorkTypes()
                        .stream()
                        .map(AsfWorkType::getName)
                        .reduce((a,b) -> a + ", " + b)
                        .orElse("")
        );

        map.put("OBJECT_NAME", objectModel.getObjectName());
        ObjectAddress address =
                objectAddressService.getByObjectId(objectModel.getId());

        ObjectCity city =
                objectCityService.getById(address.getId()); // связь по id

        String objectAddressText =
                ObjectAddressFormatter.format(address, city);

        map.put("OBJECT_ADDRESS", objectAddressText);


        map.put(
                "OBJECT_STRUCTURE_LIST",
                WordListBuilder.build(objectStructureP1, ListFormat.DOT)
        );

        map.put("HAZARD_CLASS", HazardUtils.toRoman(String.valueOf(objectModel.getHazardClass())));
        map.put("HAZARDOUS_SUBSTANCE", hazardousSubstance.getName());
        map.put("AMOUNT_HAZARDOUS_SUBSTANCE", objectModel.getAmountOfHazardousSubstance());
        map.put("HAZARDOUS_SUBSTANCE_GEN", hazardousSubstance.getName_gen());

        int count =
                technologicalBlockService.countByObjectId(objectModel.getId());

        map.put(
                "AMOUNT_TECHNOLOGICAL_BLOCK",
                RussianPlural.technologicalBlock(count)
        );

        map.put(
                "TECHNO_BLOCK_LIST",
                WordListBuilder.build(technologicalBlock, ListFormat.NO_SIGN)
        );

        String cityText =
                ObjectCityTextBuilder.buildFullDescription(city);

        map.put("OBJECT_CITY_FULL", cityText);

        return map;
    }
}