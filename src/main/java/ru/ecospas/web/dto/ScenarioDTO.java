package ru.ecospas.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioDTO {

    private Integer id;
    private String name;
    private String displayName;
    private int number;
    private String description;
    private String impactFactor;
}