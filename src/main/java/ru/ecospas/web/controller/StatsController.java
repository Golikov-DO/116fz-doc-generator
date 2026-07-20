package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.web.dto.response.stats.StatsResponse;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final OrganizationRepository organizationRepository;
    private final ObjectModelRepository objectModelRepository;

    @GetMapping
    public StatsResponse getStats() {
        return new StatsResponse(
                organizationRepository.count(),
                objectModelRepository.count()
        );
    }
}