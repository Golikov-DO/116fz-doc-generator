package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectCity;

import java.util.Optional;

public interface ObjectCityRepository {

    ObjectCity findById(int id);

}
