package ru.ecospas.web.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final OrganizationRepository organizationRepository;
    private final OrganizationAddressRepository organizationAddressRepository;
    private final OrganizationContactRepository organizationContactRepository;
    private final OrganizationSignerRepository organizationSignerRepository;

    private final ObjectModelRepository objectRepository;
    private final ObjectAddressRepository objectAddressRepository;
    private final ObjectCompositionKchsRepository objectCompositionKchsRepository;
    private final ObjectTechnologicalEquipmentRepository objectTechnologicalEquipmentRepository;
    private final ObjectStructureRepository objectStructureRepository;
    private final ObjectTechnologicalBlockRepository objectTechnologicalBlockRepository;
    private final ObjectFireEquipmentRepository objectFireEquipmentRepository;
    private final ObjectPersonsResponsibleRepository objectPersonsResponsibleRepository;
    private final ObjectImageRepository objectImageRepository;
    private final ObjectInsurancePolicyRepository objectInsurancePolicyRepository;
    private final ObjectOrderMinimumBalanceRepository objectOrderMinimumBalanceRepository;

    private final AsfRepository asfRepository;
    private final AsfCertificateRepository asfCertificateRepository;
    private final AsfCompositionDeploymentFundsRepository asfCompositionDeploymentFundsRepository;
    private final AsfDocumentImageRepository asfDocumentImageRepository;
    private final AsfPersonnelRepository asfPersonnelRepository;
    private final AsfSpecialistsRepository asfSpecialistsRepository;
    private final AsfSignerRepository asfSignerRepository;
    private final AsfWorkTypeRepository asfWorkTypeRepository;

    // ------------------------------------------------------------------------
    // ORGANIZATION
    // ------------------------------------------------------------------------

    public OrganizationData loadOrganization(int orgId) {

        Organization organization = organizationRepository
                .findById(orgId)
                .orElse(null);

        OrganizationAddress address = organizationAddressRepository
                .findByOrganizationId(orgId)
                .orElse(null);

        OrganizationSigner signer = organizationSignerRepository
                        .findByOrganizationId(orgId).orElse(null);


        List<OrganizationContact> contacts =
                organizationContactRepository
                        .findAllByOrganizationId(orgId);

        return new OrganizationData(
                organization,
                address,
                signer,
                contacts
        );
    }

    // ------------------------------------------------------------------------
    // OBJECTS
    // ------------------------------------------------------------------------

    public List<ObjectModel> loadObjects(int organizationId) {
        return objectRepository.findByOrganizationId(organizationId);
    }

    // ------------------------------------------------------------------------
    // OBJECT
    // ------------------------------------------------------------------------

    public ObjectData loadObject(int objectId) {

        ObjectModel object = objectRepository
                .findById(objectId)
                .orElse(null);

        ObjectAddress address = objectAddressRepository
                .findByObjectId(objectId)
                .orElse(null);

        List<ObjectCompositionKchs> kchs = objectCompositionKchsRepository
                        .findAllByObjectId(objectId);

        List<ObjectTechnologicalEquipment> equipment = objectTechnologicalEquipmentRepository
                        .findAllByObjectId(objectId);

        List<ObjectStructure> structures = objectStructureRepository
                        .findAllByObjectId(objectId);

        List<ObjectTechnologicalBlock> blocks = objectTechnologicalBlockRepository
                        .findAllByObjectId(objectId);

        List<ObjectFireEquipment> fireEquipment = objectFireEquipmentRepository
                        .findAllByObjectIdOrderByNumber(objectId);

        List<ObjectPersonsResponsible> responsible = objectPersonsResponsibleRepository
                        .findAllByObjectIdOrderByNumber(objectId);

        List<ObjectImage> images = objectImageRepository
                        .findAllByObjectId(objectId);

        ObjectInsurancePolicy policy = objectInsurancePolicyRepository
                        .findByObjectId(objectId)
                        .orElse(null);

        ObjectOrderMinimumBalance balance = objectOrderMinimumBalanceRepository
                        .findByObjectId(objectId)
                        .orElse(null);

        return new ObjectData(
                object,
                address,
                kchs,
                equipment,
                structures,
                blocks,
                fireEquipment,
                responsible,
                images,
                policy,
                balance
        );
    }
    // ------------------------------------------------------------------------
    // ASF
    // ------------------------------------------------------------------------

    public AsfData loadAsf(int asfId) {

        Asf asf = asfRepository
                .findById(asfId)
                .orElse(null);

        AsfCertificate certificate = asfCertificateRepository
                        .findByAsfId(asfId)
                        .orElse(null);

        AsfCompositionDeploymentFunds deployment = asfCompositionDeploymentFundsRepository
                        .findByAsfId(asfId)
                        .orElse(null);

        List<AsfDocumentImage> images = asfDocumentImageRepository
                        .findAllByAsfId(asfId);

        AsfPersonnel personnel = asfPersonnelRepository
                        .findByAsfId(asfId)
                        .orElse(null);

        AsfSpecialists specialists = asfSpecialistsRepository
                        .findByAsfId(asfId)
                        .orElse(null);

        List<AsfSigner> signers = asfSignerRepository.findAllByAsfId(asfId);

        List<AsfWorkType> workTypes = asfWorkTypeRepository.findAllByAsfId(asfId);

        return new AsfData(
                asf,
                certificate,
                deployment,
                images,
                personnel,
                specialists,
                signers,
                workTypes
        );
    }

    // ------------------------------------------------------------------------
    // DTO
    // ------------------------------------------------------------------------

    public record OrganizationData(
            Organization org,
            OrganizationAddress addr,
            OrganizationSigner signer,
            List<OrganizationContact> contacts
    ) {
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
            List<AsfWorkType> workTypes
    ) {
    }
}