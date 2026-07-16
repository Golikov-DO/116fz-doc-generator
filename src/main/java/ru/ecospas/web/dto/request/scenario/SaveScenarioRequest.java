package ru.ecospas.web.dto.request.scenario;

public record SaveScenarioRequest(

        String name,
        String description,
        String impactFactor

) {
}