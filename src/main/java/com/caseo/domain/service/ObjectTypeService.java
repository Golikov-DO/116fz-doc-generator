package com.caseo.domain.service;

import com.caseo.domain.model.ObjectType;
import com.caseo.domain.repository.ObjectTypeRepository;

public class ObjectTypeService {
    private final ObjectTypeRepository objectTypeRepository;

    public ObjectTypeService(ObjectTypeRepository objectTypeRepository) {
        this.objectTypeRepository = objectTypeRepository;
    }

    public ObjectType getObjectType(int id) {
        return objectTypeRepository.findByObjectId(id);
    }
}
