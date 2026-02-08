package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectCity;

public interface ObjectCityRepository {

    ObjectCity findByObjectId(int id);

}
