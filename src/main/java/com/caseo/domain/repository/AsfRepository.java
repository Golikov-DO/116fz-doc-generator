package com.caseo.domain.repository;

import com.caseo.domain.model.Asf;

public interface AsfRepository {

    Asf findByOrganizationId(int organizationId);

}
