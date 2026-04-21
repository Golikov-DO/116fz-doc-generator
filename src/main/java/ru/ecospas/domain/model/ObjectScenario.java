package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "object_scenario")
@Getter
@Setter
@NoArgsConstructor
public class ObjectScenario implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "structure_id")
    private ObjectStructure structure;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ScenarioType type;
}