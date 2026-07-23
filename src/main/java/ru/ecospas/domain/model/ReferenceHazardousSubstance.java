package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table (name = "hazardous_substance")
public class ReferenceHazardousSubstance implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String nameShort;

    @OneToMany(mappedBy = "hazardousSubstance")
    private List<ObjectModel> objects = new ArrayList<>();

    @OneToMany(
            mappedBy = "substance",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubstanceHazardousParamValue> values = new ArrayList<>();
}
