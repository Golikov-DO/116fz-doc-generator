package ru.ecospas.domain.service;

import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.*;

public class ObjectDeleteService {

    private final ChildService<ObjectAddress> addressService;
    private final ChildService<ObjectInsurancePolicy> policyService;
    private final ChildService<ObjectOrderMinimumBalance> balanceService;
    private final ChildService<ObjectCompositionKchs> kchsService;
    private final ChildService<ObjectTechnologicalEquipment> equipmentService;
    private final ChildService<ObjectStructure> structureService;
    private final ChildService<ObjectTechnologicalBlock> technoBlockService;
    private final ChildService<ObjectPersonsResponsible> personsService;
    private final ChildService<ObjectImage> imageService;
    private final ParentService<ObjectModel> objectService;

    public ObjectDeleteService(InternalServices services) {
        this.objectService = services.getParentService(ObjectModel.class);

        this.addressService = services.getChildService(ObjectAddress.class);
        this.policyService = services.getChildService(ObjectInsurancePolicy.class);
        this.balanceService = services.getChildService(ObjectOrderMinimumBalance.class);
        this.kchsService = services.getChildService(ObjectCompositionKchs.class);
        this.equipmentService = services.getChildService(ObjectTechnologicalEquipment.class);
        this.structureService = services.getChildService(ObjectStructure.class);
        this.technoBlockService = services.getChildService(ObjectTechnologicalBlock.class);
        this.personsService = services.getChildService(ObjectPersonsResponsible.class);
        this.imageService = services.getChildService(ObjectImage.class);
    }

    public void delete(int objectId) {

        ObjectAddress address = addressService.getOneByParentId(objectId);
        if (address != null) addressService.deleteById(address.getId());

        ObjectInsurancePolicy policy = policyService.getOneByParentId(objectId);
        if (policy != null) policyService.deleteById(policy.getId());

        ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(objectId);
        if (balance != null) balanceService.deleteById(balance.getId());

        kchsService.getManyByParentId(objectId)
                .forEach(e -> kchsService.deleteById(e.getId()));

        equipmentService.getManyByParentId(objectId)
                .forEach(e -> equipmentService.deleteById(e.getId()));

        structureService.getManyByParentId(objectId)
                .forEach(e -> structureService.deleteById(e.getId()));

        technoBlockService.getManyByParentId(objectId)
                .forEach(e -> technoBlockService.deleteById(e.getId()));

        personsService.getManyByParentId(objectId)
                .forEach(e -> personsService.deleteById(e.getId()));

        imageService.getManyByParentId(objectId)
                .forEach(e -> imageService.deleteById(e.getId()));

        objectService.deleteById(objectId);
    }
}