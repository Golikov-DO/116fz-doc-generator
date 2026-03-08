package com.caseo.app;

import com.caseo.domain.service.WordGenerationService;

public record ApplicationContext(
        WordGenerationService wordGenerationService,
        InternalServices internalServices
) {}