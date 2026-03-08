package com.caseo.web.helper;

import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private final InternalServices services;

    public DataLoader(InternalServices services) {
        this.services = services;
    }

    // Загрузка организации
    public OrganizationData loadOrganization(int orgId) {
        ParentService<Organization> orgService = services.getParentService(Organization.class);
        Organization org = orgService.getOneById(orgId);

        ChildService<OrganizationAddress> addrService = services.getChildService(OrganizationAddress.class);
        OrganizationAddress addr = addrService.getOneByParentId(orgId);

        ChildService<OrganizationSigner> signerService = services.getChildService(OrganizationSigner.class);
        OrganizationSigner signer = signerService.getOneByParentId(orgId);

        ChildService<OrganizationContact> contactService = services.getChildService(OrganizationContact.class);
        List<OrganizationContact> contacts = contactService.getManyByParentId(orgId);

        return new OrganizationData(org, addr, signer, contacts);
    }

    // Загрузка объектов
    public ObjectsData loadObjects(int orgId) {
        ChildService<ObjectModel> objService = services.getChildService(ObjectModel.class);
        List<ObjectModel> objects = objService.getManyByParentId(orgId);

        ChildService<ObjectAddress> objAddrService = services.getChildService(ObjectAddress.class);
        ChildService<ObjectCompositionKchs> kchsService = services.getChildService(ObjectCompositionKchs.class);
        ChildService<ObjectTechnologicalEquipment> equipService = services.getChildService(ObjectTechnologicalEquipment.class);
        ChildService<ObjectStructure> structService = services.getChildService(ObjectStructure.class);
        ChildService<ObjectFireEquipment> fireService = services.getChildService(ObjectFireEquipment.class);
        ChildService<ObjectRegionalAuthorities> regionalService = services.getChildService(ObjectRegionalAuthorities.class);
        ChildService<ObjectInsurancePolicy> policyService = services.getChildService(ObjectInsurancePolicy.class);
        ChildService<ObjectOrderMinimumBalance> balanceService = services.getChildService(ObjectOrderMinimumBalance.class);
        ChildService<ObjectType> typeService = services.getChildService(ObjectType.class);

        List<ObjectAddress> addresses = new ArrayList<>();
        List<List<ObjectCompositionKchs>> kchsLists = new ArrayList<>();
        List<List<ObjectTechnologicalEquipment>> equipmentLists = new ArrayList<>();
        List<List<ObjectStructure>> structureLists = new ArrayList<>();
        List<List<ObjectFireEquipment>> fireLists = new ArrayList<>();
        List<List<ObjectRegionalAuthorities>> authoritiesLists = new ArrayList<>();
        List<ObjectInsurancePolicy> policies = new ArrayList<>();
        List<ObjectOrderMinimumBalance> balances = new ArrayList<>();
        List<ObjectType> types = new ArrayList<>();

        for (ObjectModel obj : objects) {
            addresses.add(objAddrService.getOneByParentId(obj.getId()));
            kchsLists.add(kchsService.getManyByParentId(obj.getId()));
            equipmentLists.add(equipService.getManyByParentId(obj.getId()));
            structureLists.add(structService.getManyByParentId(obj.getId()));
            fireLists.add(fireService.getManyByParentId(obj.getId()));
            authoritiesLists.add(regionalService.getManyByParentId(obj.getId()));
            policies.add(policyService.getOneByParentId(obj.getId()));
            balances.add(balanceService.getOneByParentId(obj.getId()));
            types.add(typeService.getOneByParentId(obj.getId()));
        }

        return new ObjectsData(objects, addresses, kchsLists, equipmentLists, 
                              structureLists, fireLists, authoritiesLists, 
                              policies, balances, types);
    }

    // Загрузка АСФ
    public AsfData loadAsf(int asfId) {
        ParentService<Asf> asfService = services.getParentService(Asf.class);
        Asf asf = asfService.getOneById(asfId);

        ChildService<AsfCertificate> certService = services.getChildService(AsfCertificate.class);
        List<AsfCertificate> certificates = certService.getManyByParentId(asfId);
        AsfCertificate certificate = certificates.isEmpty() ? null : certificates.getFirst();

        ChildService<AsfCompositionDeploymentFunds> fundsService = services.getChildService(AsfCompositionDeploymentFunds.class);
        List<AsfCompositionDeploymentFunds> fundsList = fundsService.getManyByParentId(asfId);
        AsfCompositionDeploymentFunds deployment = fundsList.isEmpty() ? null : fundsList.getFirst();

        ChildService<AsfDocumentImage> imageService = services.getChildService(AsfDocumentImage.class);
        List<AsfDocumentImage> images = imageService.getManyByParentId(asfId);

        ChildService<AsfPersonnel> personnelService = services.getChildService(AsfPersonnel.class);
        List<AsfPersonnel> personnelList = personnelService.getManyByParentId(asfId);
        AsfPersonnel personnel = personnelList.isEmpty() ? null : personnelList.getFirst();

        ChildService<AsfSpecialists> specialistsService = services.getChildService(AsfSpecialists.class);
        List<AsfSpecialists> specialistsList = specialistsService.getManyByParentId(asfId);
        AsfSpecialists specialists = specialistsList.isEmpty() ? null : specialistsList.getFirst();

        ChildService<AsfSigner> signerService = services.getChildService(AsfSigner.class);
        List<AsfSigner> signers = signerService.getManyByParentId(asfId);

        ChildService<AsfWorkType> workTypeService = services.getChildService(AsfWorkType.class);
        List<AsfWorkType> workTypes = workTypeService.getManyByParentId(asfId);

        return new AsfData(asf, certificate, deployment, images, personnel, 
                          specialists, signers, workTypes);
    }

    // Классы-обертки для данных
    public record OrganizationData(Organization org, OrganizationAddress addr, 
                                   OrganizationSigner signer, List<OrganizationContact> contacts) {}

    public record ObjectsData(List<ObjectModel> objects, List<ObjectAddress> addresses,
                              List<List<ObjectCompositionKchs>> kchsLists,
                              List<List<ObjectTechnologicalEquipment>> equipmentLists,
                              List<List<ObjectStructure>> structureLists,
                              List<List<ObjectFireEquipment>> fireLists,
                              List<List<ObjectRegionalAuthorities>> authoritiesLists,
                              List<ObjectInsurancePolicy> policies,
                              List<ObjectOrderMinimumBalance> balances,
                              List<ObjectType> types) {}

    public record AsfData(Asf asf, AsfCertificate certificate, 
                          AsfCompositionDeploymentFunds deployment,
                          List<AsfDocumentImage> images, AsfPersonnel personnel,
                          AsfSpecialists specialists, List<AsfSigner> signers,
                          List<AsfWorkType> workTypes) {}
}