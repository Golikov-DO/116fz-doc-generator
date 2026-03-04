package com.caseo.app;

import com.caseo.domain.repository.*;
import com.caseo.domain.service.*;

public record ApplicationContext(
        AsfSignerRepository asfSignerRepository,
        WordGenerationService wordGenerationService,
        OrganizationService organizationService,
        ObjectService objectService,
        InternalServices internalServices
) {
}