package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.repository.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectDeleteService {

    private final ObjectModelRepository objectRepository;

    private final ObjectAddressRepository addressRepository;
    private final ObjectInsurancePolicyRepository policyRepository;
    private final ObjectOrderMinimumBalanceRepository balanceRepository;
    private final ObjectCompositionKchsRepository kchsRepository;
    private final ObjectTechnologicalEquipmentRepository equipmentRepository;
    private final ObjectStructureRepository structureRepository;
    private final ObjectTechnologicalBlockRepository technoBlockRepository;
    private final ObjectPersonsResponsibleRepository personsRepository;
    private final ObjectImageRepository imageRepository;

    public void delete(int objectId) {

        addressRepository.findByObjectId(objectId).ifPresent(addressRepository::delete);

        policyRepository.findByObjectId(objectId).ifPresent(policyRepository::delete);

        balanceRepository.findByObjectId(objectId).ifPresent(balanceRepository::delete);

        kchsRepository.deleteAllByObjectId(objectId);

        equipmentRepository.deleteAllByObjectId(objectId);

        structureRepository.deleteAllByObjectId(objectId);

        technoBlockRepository.deleteAllByObjectId(objectId);

        personsRepository.deleteAllByObjectId(objectId);

        imageRepository.deleteAllByObjectId(objectId);

        objectRepository.deleteById(objectId);
    }
}