package ru.ecospas.web.dto.response.scenario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordScenarioResponse {

    private Integer id;
    private String name;
    private String displayName;
    private int number;
    private String description;
    private String impactFactor;
}