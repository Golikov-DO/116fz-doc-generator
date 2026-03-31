package com.caseo.web.helper;

import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
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

    // Загрузка объекта
    public ObjectData loadObject(int objectId) {

        ParentService<ObjectModel> objService = services.getParentService(ObjectModel.class);
        ObjectModel object = objService.getOneById(objectId);

        ParentService<ReferenceCity> cityService = services.getParentService(ReferenceCity.class);
        ReferenceCity city = cityService.getOneById(objectId);

        ChildService<ObjectAddress> addrService = services.getChildService(ObjectAddress.class);
        ObjectAddress address = addrService.getOneByParentId(objectId);

        ChildService<ObjectCompositionKchs> kchsService = services.getChildService(ObjectCompositionKchs.class);
        List<ObjectCompositionKchs> kchsList = kchsService.getManyByParentId(objectId);

        ChildService<ObjectTechnologicalEquipment> equipService = services.getChildService(ObjectTechnologicalEquipment.class);
        List<ObjectTechnologicalEquipment> equipmentList = equipService.getManyByParentId(objectId);

        ChildService<ObjectStructure> structService = services.getChildService(ObjectStructure.class);
        List<ObjectStructure> structureList = structService.getManyByParentId(objectId);

        ChildService<ObjectFireEquipment> fireService = services.getChildService(ObjectFireEquipment.class);
        List<ObjectFireEquipment> fireList = fireService.getManyByParentId(objectId);

        ChildService<ObjectRegionalAuthorities> regionalService = services.getChildService(ObjectRegionalAuthorities.class);
        List<ObjectRegionalAuthorities> authoritiesList = regionalService.getManyByParentId(city.getId());

        ChildService<ObjectInsurancePolicy> policyService = services.getChildService(ObjectInsurancePolicy.class);
        ObjectInsurancePolicy policy = policyService.getOneByParentId(objectId);

        ChildService<ObjectOrderMinimumBalance> balanceService = services.getChildService(ObjectOrderMinimumBalance.class);
        ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(objectId);

        ChildService<ObjectType> typeService = services.getChildService(ObjectType.class);
        ObjectType type = typeService.getOneByParentId(objectId);

        return new ObjectData(object, address, kchsList, equipmentList, structureList,
                fireList, authoritiesList, policy, balance, type
        );
    }

    public List<ObjectModel> loadObjects(int orgId) {
        ChildService<ObjectModel> objService = services.getChildService(ObjectModel.class);
        return objService.getManyByParentId(orgId);
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

    public record ObjectData(
            ObjectModel object,
            ObjectAddress address,
            List<ObjectCompositionKchs> kchsList,
            List<ObjectTechnologicalEquipment> equipmentList,
            List<ObjectStructure> structureList,
            List<ObjectFireEquipment> fireList,
            List<ObjectRegionalAuthorities> authoritiesList,
            ObjectInsurancePolicy policy,
            ObjectOrderMinimumBalance balance,
            ObjectType type
    ) {}

    public record AsfData(Asf asf, AsfCertificate certificate, 
                          AsfCompositionDeploymentFunds deployment,
                          List<AsfDocumentImage> images, AsfPersonnel personnel,
                          AsfSpecialists specialists, List<AsfSigner> signers,
                          List<AsfWorkType> workTypes) {}
}