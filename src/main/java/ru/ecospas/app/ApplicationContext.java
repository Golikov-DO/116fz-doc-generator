package ru.ecospas.app;

import ru.ecospas.domain.service.WordGenerationService;

public record ApplicationContext(
        WordGenerationService wordGenerationService,
        InternalServices internalServices
) {}