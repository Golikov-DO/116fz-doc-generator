package ru.ecospas.web.mapper.object;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.*;
import ru.ecospas.web.dto.request.object.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ObjectRequestMapper {

    private final ReferenceCityRepository cityRepository;
    private final ReferenceTypeRepository typeRepository;
    private final ReferenceHazardousSubstanceRepository substanceRepository;
    private final AsfRepository asfRepository;
    private final ScenarioRepository scenarioRepository;

    public void toObject(
            SaveObjectRequest request,
            ObjectModel object
    ) {

        object.setObjectFullName(request.objectFullName());
        object.setHazardClass(request.hazardClass());
        object.setAmountOfHazardousSubstance(request.amountOfHazardousSubstance());
        object.setNearestFireStation(request.nearestFireStation());
        object.setDepartmentGoChsCity(request.departmentGoChsCity());
        object.setEmergencyCommission(request.emergencyCommission());
        object.setAsfSignerId(request.asfSignerId());
        object.setArrivalTime(request.arrivalTime());

        object.setCity(
                request.cityId() == null
                        ? null
                        : cityRepository.getReferenceById(request.cityId())
        );

        object.setType(
                request.typeId() == null
                        ? null
                        : typeRepository.getReferenceById(request.typeId())
        );

        object.setHazardousSubstance(
                request.hazardousSubstanceId() == null
                        ? null
                        : substanceRepository.getReferenceById(request.hazardousSubstanceId())
        );

        object.setAsf(
                request.asfId() == null
                        ? null
                        : asfRepository.getReferenceById(request.asfId())
        );
    }

    public void toAddress(
            ObjectAddressRequest request,
            ObjectAddress address
    ) {

        address.setAddressIndex(request.addressIndex());
        address.setConstituentEntity(request.constituentEntity());
        address.setAreaHierarchy(request.areaHierarchy());
        address.setCity(request.city());
        address.setStreet(request.street());
        address.setHouse(request.house());
        address.setCoordinates(request.coordinates());
    }

    public void toInsurancePolicy(
            ObjectInsurancePolicyRequest request,
            ObjectInsurancePolicy policy
    ) {

        policy.setNumber(request.number());
        policy.setValidUntil(request.validUntil());
    }

    public void toMinimumBalance(
            ObjectOrderMinimumBalanceRequest request,
            ObjectOrderMinimumBalance balance
    ) {

        balance.setNumber(request.number());
        balance.setDate(request.date());
    }

    public List<ObjectCompositionKchs> toCompositionKchs(
            List<ObjectCompositionKchsRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectCompositionKchs> result = new ArrayList<>();

        for (ObjectCompositionKchsRequest request : requests) {

            ObjectCompositionKchs item = new ObjectCompositionKchs();

            item.setId(request.id());
            item.setNumber(request.number());
            item.setPosition(request.position());
            item.setFullName(request.fullName());
            item.setWorkPhone(request.workPhone());
            item.setCellPhone(request.cellPhone());
            item.setHomeAddress(request.homeAddress());

            result.add(item);
        }

        return result;
    }

    public List<ObjectPersonsResponsible> toResponsiblePersons(
            List<ObjectPersonsResponsibleRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectPersonsResponsible> result = new ArrayList<>();

        for (ObjectPersonsResponsibleRequest request : requests) {

            ObjectPersonsResponsible item = new ObjectPersonsResponsible();

            item.setId(request.id());
            item.setNumber(request.number());
            item.setFullName(request.fullName());
            item.setPosition(request.position());

            result.add(item);
        }

        return result;
    }

    public List<ObjectFireEquipment> toFireEquipments(
            List<ObjectFireEquipmentRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectFireEquipment> result = new ArrayList<>();

        for (ObjectFireEquipmentRequest request : requests) {

            ObjectFireEquipment item = new ObjectFireEquipment();

            item.setId(request.id());
            item.setNumber(request.number());
            item.setProductName(request.productName());
            item.setQuantity(request.quantity());
            item.setLocation(request.location());

            result.add(item);
        }

        return result;
    }

    public List<ObjectTechnologicalEquipment> toTechnologicalEquipments(
            List<ObjectTechnologicalEquipmentRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectTechnologicalEquipment> result = new ArrayList<>();

        for (ObjectTechnologicalEquipmentRequest request : requests) {

            ObjectTechnologicalEquipment item = new ObjectTechnologicalEquipment();

            item.setId(request.id());
            item.setNum(request.num());
            item.setName(request.name());
            item.setCharacteristics(request.characteristics());

            result.add(item);
        }

        return result;
    }

    public List<ObjectTechnologicalBlock> toTechnologicalBlocks(
            List<ObjectTechnologicalBlockRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectTechnologicalBlock> result = new ArrayList<>();

        for (ObjectTechnologicalBlockRequest request : requests) {

            ObjectTechnologicalBlock item = new ObjectTechnologicalBlock();

            item.setId(request.id());
            item.setNum(request.num());
            item.setName(request.name());

            result.add(item);
        }

        return result;
    }

    public List<ObjectStructure> toStructures(
            List<ObjectStructureRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectStructure> result = new ArrayList<>();

        for (ObjectStructureRequest request : requests) {

            ObjectStructure structure = new ObjectStructure();

            structure.setId(request.id());
            structure.setNum(request.num());
            structure.setName(request.name());

            result.add(structure);
        }

        return result;
    }

    private List<ObjectScenario> toScenarios(
            List<ObjectScenarioRequest> requests,
            ObjectStructure structure
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectScenario> result = new ArrayList<>();

        for (ObjectScenarioRequest request : requests) {

            ObjectScenario item = new ObjectScenario();

            item.setStructure(structure);

            item.setScenario(
                    scenarioRepository.getReferenceById(
                            request.scenarioId()
                    )
            );

            item.setType(
                    ScenarioType.valueOf(request.type())
            );

            result.add(item);
        }

        return result;
    }

    public List<ObjectImage> toImages(
            List<ObjectImageRequest> requests
    ) {

        if (requests == null) {
            return new ArrayList<>();
        }

        List<ObjectImage> result = new ArrayList<>();

        for (ObjectImageRequest request : requests) {

            ObjectImage image = new ObjectImage();

            image.setId(request.id());
            image.setCaption(request.caption());
            image.setLinkText(request.linkText());
            image.setGroupKey(request.groupKey());

            result.add(image);
        }

        return result;
    }
}