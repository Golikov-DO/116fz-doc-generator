package ru.ecospas.web.helper;

import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.List;

public class DataLoader {

    private final InternalServices services;

    public DataLoader(InternalServices services) {
        this.services = services;
    }

    // Organization load
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

    // Object load
    public ObjectData loadObject(int objectId) {

        ParentService<ObjectModel> objectService = services.getParentService(ObjectModel.class);
        ObjectModel object = objectService.getOneById(objectId);

        ChildService<ObjectAddress> addressService = services.getChildService(ObjectAddress.class);
        ObjectAddress address = addressService.getOneByParentId(objectId);

        ChildService<ObjectCompositionKchs> kchsService = services.getChildService(ObjectCompositionKchs.class);
        List<ObjectCompositionKchs> kchsList = kchsService.getManyByParentId(objectId);

        ChildService<ObjectTechnologicalEquipment> equipmentService = services.getChildService(ObjectTechnologicalEquipment.class);
        List<ObjectTechnologicalEquipment> equipmentList = equipmentService.getManyByParentId(objectId);

        ChildService<ObjectStructure> structureService = services.getChildService(ObjectStructure.class);
        List<ObjectStructure> structureList = structureService.getManyByParentId(objectId);

        ChildService<ObjectTechnologicalBlock> technoBlockService = services.getChildService(ObjectTechnologicalBlock.class);
        List<ObjectTechnologicalBlock> technoBlockList = technoBlockService.getManyByParentId(objectId);

        ChildService<ObjectFireEquipment> fireEquipmentService = services.getChildService(ObjectFireEquipment.class);
        List<ObjectFireEquipment> fireEquipmentList = fireEquipmentService.getManyByParentId(objectId);

        ChildService<ObjectPersonsResponsible> personsService = services.getChildService(ObjectPersonsResponsible.class);
        List<ObjectPersonsResponsible> personsResponseList = personsService.getManyByParentId(objectId);

        ChildService<ObjectImage> imageService = services.getChildService(ObjectImage.class);
        List<ObjectImage> images = imageService.getManyByParentId(objectId);

        ChildService<ObjectInsurancePolicy> policyService = services.getChildService(ObjectInsurancePolicy.class);
        ObjectInsurancePolicy policy = policyService.getOneByParentId(objectId);

        ChildService<ObjectOrderMinimumBalance> balanceService = services.getChildService(ObjectOrderMinimumBalance.class);
        ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(objectId);

        return new ObjectData(
                object,
                address,
                kchsList,
                equipmentList,
                structureList,
                technoBlockList,
                fireEquipmentList,
                personsResponseList,
                images,
                policy,
                balance
        );
    }

    // Objects load
    public List<ObjectModel> loadObjects(int orgId) {
        ChildService<ObjectModel> objService = services.getChildService(ObjectModel.class);
        return objService.getManyByParentId(orgId);
    }

    // ASF load
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

        return new AsfData(asf, certificate, deployment, images, personnel, specialists, signers, workTypes);
    }

    // Data wrapper classes
    public record OrganizationData(
            Organization org,
            OrganizationAddress addr,
            OrganizationSigner signer,
            List<OrganizationContact> contacts) {
    }

    public record ObjectData(
            ObjectModel object,
            ObjectAddress address,
            List<ObjectCompositionKchs> kchsList,
            List<ObjectTechnologicalEquipment> equipmentList,
            List<ObjectStructure> structureList,
            List<ObjectTechnologicalBlock> technoBlockList,
            List<ObjectFireEquipment> fireEquipmentList,
            List<ObjectPersonsResponsible> personsResponseList,
            List<ObjectImage> images,
            ObjectInsurancePolicy policy,
            ObjectOrderMinimumBalance balance
    ) {
    }

    public record AsfData(
            Asf asf,
            AsfCertificate certificate,
            AsfCompositionDeploymentFunds deployment,
            List<AsfDocumentImage> images,
            AsfPersonnel personnel,
            AsfSpecialists specialists,
            List<AsfSigner> signers,
            List<AsfWorkType> workTypes) {
    }
}