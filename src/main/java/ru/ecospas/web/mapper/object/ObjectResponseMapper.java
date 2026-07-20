package ru.ecospas.web.mapper.object;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.*;
import ru.ecospas.web.dto.response.object.*;

import java.util.Collections;
import java.util.List;

@Component
public class ObjectResponseMapper {

    public ObjectResponse toResponse(ObjectModel object) {

        if (object == null) {
            return null;
        }

        return new ObjectResponse(

                object.getId(),

                object.getOrganization() == null ? null : object.getOrganization().getId(),
                object.getAsf() == null ? null : object.getAsf().getId(),
                object.getCity() == null ? null : object.getCity().getId(),
                object.getType() == null ? null : object.getType().getId(),
                object.getHazardousSubstance() == null ? null : object.getHazardousSubstance().getId(),
                object.getAsfSignerId(),
                object.getHazardClass(),
                object.getObjectFullName(),
                object.getAmountOfHazardousSubstance(),
                object.getNearestFireStation(),
                object.getDepartmentGoChsCity(),
                object.isEmergencyCommission(),
                object.getArrivalTime(),
                toAddress(object.getAddress()),
                toInsurancePolicy(object.getInsurancePolicy()),
                toMinimumBalance(object.getMinimumBalance()),
                toCompositionKchs(object.getCompositionKchs()),
                toResponsiblePersons(object.getResponsiblePersons()),
                toFireEquipments(object.getFireEquipments()),
                toTechnologicalEquipments(object.getTechnologicalEquipments()),
                toTechnologicalBlocks(object.getTechnologicalBlocks()),
                toStructures(object.getStructures()),
                toImages(object.getImages())
        );
    }

    public ObjectListResponse toListResponse(ObjectModel object) {

        if (object == null) {
            return null;
        }

        return new ObjectListResponse(
                object.getId(),
                object.getObjectFullName()
        );
    }

    public List<ObjectListResponse> toListResponses(
            List<ObjectModel> objects
    ) {
        return objects.stream()
                .map(this::toListResponse)
                .toList();
    }

    private ObjectAddressResponse toAddress(ObjectAddress address) {

        if (address == null) {
            return null;
        }

        return new ObjectAddressResponse(
                address.getAddressIndex(),
                address.getConstituentEntity(),
                address.getAreaHierarchy(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getCoordinates()
        );
    }

    private ObjectInsurancePolicyResponse toInsurancePolicy(ObjectInsurancePolicy policy) {

        if (policy == null) {
            return null;
        }

        return new ObjectInsurancePolicyResponse(
                policy.getNumber(),
                policy.getValidUntil()
        );
    }

    private ObjectOrderMinimumBalanceResponse toMinimumBalance(ObjectOrderMinimumBalance balance) {

        if (balance == null) {
            return null;
        }

        return new ObjectOrderMinimumBalanceResponse(
                balance.getNumber(),
                balance.getDate()
        );
    }

    private List<ObjectCompositionKchsResponse> toCompositionKchs(
            List<ObjectCompositionKchs> list
    ) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectCompositionKchsResponse(
                        item.getId(),
                        item.getNumber(),
                        item.getPosition(),
                        item.getFullName(),
                        item.getWorkPhone(),
                        item.getCellPhone(),
                        item.getHomeAddress()
                ))
                .toList();
    }

    private List<ObjectPersonsResponsibleResponse> toResponsiblePersons(
            List<ObjectPersonsResponsible> list
    ) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectPersonsResponsibleResponse(
                        item.getId(),
                        item.getNumber(),
                        item.getFullName(),
                        item.getPosition()
                ))
                .toList();
    }

    private List<ObjectFireEquipmentResponse> toFireEquipments(List<ObjectFireEquipment> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectFireEquipmentResponse(
                        item.getId(),
                        item.getNumber(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getLocation()
                ))
                .toList();
    }

    private List<ObjectTechnologicalEquipmentResponse> toTechnologicalEquipments(
            List<ObjectTechnologicalEquipment> list
    ) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectTechnologicalEquipmentResponse(
                        item.getId(),
                        item.getNum(),
                        item.getName(),
                        item.getCharacteristics()
                ))
                .toList();
    }

    private List<ObjectTechnologicalBlockResponse> toTechnologicalBlocks(
            List<ObjectTechnologicalBlock> list
    ) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectTechnologicalBlockResponse(
                        item.getId(),
                        item.getNum(),
                        item.getName()
                ))
                .toList();
    }

    private List<ObjectStructureResponse> toStructures( List<ObjectStructure> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectStructureResponse(
                        item.getId(),
                        item.getNum(),
                        item.getName(),
                        item.getLikelyIds(),
                        item.getDangerousIds()
                ))
                .toList();
    }

    private List<ObjectImageResponse> toImages(List<ObjectImage> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> new ObjectImageResponse(
                        item.getId(),
                        item.getGroupKey(),
                        item.getCaption(),
                        item.getLinkText()
                ))
                .toList();
    }
}