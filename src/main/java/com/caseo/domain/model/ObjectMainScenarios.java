package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "object")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object_main_scenarios")
public class ObjectMainScenarios implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    @Column(name = "name_equipment")
    private String equipmentName;

    private String event;

    @Column(name = "list_scenarios")
    private String scenariosList;
}