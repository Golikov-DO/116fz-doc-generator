package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectType;

public interface ObjectTypeRepository {
    ObjectType findByObjectId(int objectId);
}
