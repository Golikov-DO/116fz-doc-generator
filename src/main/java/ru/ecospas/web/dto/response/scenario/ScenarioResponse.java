package ru.ecospas.web.dto.response.scenario;

public record ScenarioResponse(

        Integer id,
        String name,
        String description,
        String impactFactor

) {
}