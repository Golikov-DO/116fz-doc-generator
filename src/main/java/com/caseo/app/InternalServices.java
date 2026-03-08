package com.caseo.app;

import com.caseo.domain.model.*;
import com.caseo.domain.service.ParentService;
import com.caseo.domain.service.ChildService;

import java.util.HashMap;
import java.util.Map;

public class InternalServices {

    private final Map<Class<?>, Object> services = new HashMap<>();

    public InternalServices(RepositoryContext repoContext) {
        // === РОДИТЕЛИ (используем getParent) ===
        services.put(Asf.class, new ParentService<>(repoContext.getParent(Asf.class)));
        services.put(ObjectHazardousSubstance.class, new ParentService<>(repoContext.getParent(ObjectHazardousSubstance.class)));
        services.put(Organization.class, new ParentService<>(repoContext.getParent(Organization.class)));
        services.put(ReferenceCity.class, new ParentService<>(repoContext.getParent(ReferenceCity.class)));
        services.put(ReferenceEmergencyServices.class, new ParentService<>(repoContext.getParent(ReferenceEmergencyServices.class)));
        services.put(ReferenceTableTitle.class, new ParentService<>(repoContext.getParent(ReferenceTableTitle.class)));

        // === ДЕТИ (используем getChild) ===
        services.put(AsfCertificate.class, new ChildService<>(repoContext.getChild(AsfCertificate.class)));
        services.put(AsfCompositionDeploymentFunds.class, new ChildService<>(repoContext.getChild(AsfCompositionDeploymentFunds.class)));
        services.put(AsfDocumentImage.class, new ChildService<>(repoContext.getChild(AsfDocumentImage.class)));
        services.put(AsfPersonnel.class, new ChildService<>(repoContext.getChild(AsfPersonnel.class)));
        services.put(AsfSigner.class, new ChildService<>(repoContext.getChild(AsfSigner.class)));
        services.put(AsfSpecialists.class, new ChildService<>(repoContext.getChild(AsfSpecialists.class)));
        services.put(AsfWorkType.class, new ChildService<>(repoContext.getChild(AsfWorkType.class)));

        services.put(ObjectAccidentScenarios.class, new ChildService<>(repoContext.getChild(ObjectAccidentScenarios.class)));
        services.put(ObjectAddress.class, new ChildService<>(repoContext.getChild(ObjectAddress.class)));
        services.put(ObjectCompositionKchs.class, new ChildService<>(repoContext.getChild(ObjectCompositionKchs.class)));
        services.put(ObjectFireEquipment.class, new ChildService<>(repoContext.getChild(ObjectFireEquipment.class)));
        services.put(ObjectHazardousParam.class, new ChildService<>(repoContext.getChild(ObjectHazardousParam.class)));
        services.put(ObjectHazardousParamValue.class, new ChildService<>(repoContext.getChild(ObjectHazardousParamValue.class)));
        services.put(ObjectImage.class, new ChildService<>(repoContext.getChild(ObjectImage.class)));
        services.put(ObjectInsurancePolicy.class, new ChildService<>(repoContext.getChild(ObjectInsurancePolicy.class)));
        services.put(ObjectMainScenarios.class, new ChildService<>(repoContext.getChild(ObjectMainScenarios.class)));
        services.put(ObjectModel.class, new ChildService<>(repoContext.getChild(ObjectModel.class)));
        services.put(ObjectOrderMinimumBalance.class, new ChildService<>(repoContext.getChild(ObjectOrderMinimumBalance.class)));
        services.put(ObjectPersonsResponsible.class, new ChildService<>(repoContext.getChild(ObjectPersonsResponsible.class)));
        services.put(ObjectRegionalAuthorities.class, new ChildService<>(repoContext.getChild(ObjectRegionalAuthorities.class)));
        services.put(ObjectStructure.class, new ChildService<>(repoContext.getChild(ObjectStructure.class)));
        services.put(ObjectTechnologicalBlock.class, new ChildService<>(repoContext.getChild(ObjectTechnologicalBlock.class)));
        services.put(ObjectTechnologicalEquipment.class, new ChildService<>(repoContext.getChild(ObjectTechnologicalEquipment.class)));
        services.put(ObjectType.class, new ChildService<>(repoContext.getChild(ObjectType.class)));

        services.put(OrganizationAddress.class, new ChildService<>(repoContext.getChild(OrganizationAddress.class)));
        services.put(OrganizationContact.class, new ChildService<>(repoContext.getChild(OrganizationContact.class)));
        services.put(OrganizationSigner.class, new ChildService<>(repoContext.getChild(OrganizationSigner.class)));
    }

    @SuppressWarnings("unchecked")
    public <T> ParentService<T> getParentService(Class<T> entityClass) {
        Object service = services.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("Нет сервиса для " + entityClass);
        }
        if (!(service instanceof ParentService)) {
            throw new IllegalArgumentException("Сервис для " + entityClass + " не является ParentService");
        }
        return (ParentService<T>) service;
    }

    @SuppressWarnings("unchecked")
    public <T> ChildService<T> getChildService(Class<T> entityClass) {
        Object service = services.get(entityClass);
        if (service == null) {
            throw new IllegalArgumentException("Нет сервиса для " + entityClass);
        }
        if (!(service instanceof ChildService)) {
            throw new IllegalArgumentException("Сервис для " + entityClass + " не является ChildService");
        }
        return (ChildService<T>) service;
    }
}