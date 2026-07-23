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
@Table(name = "hazardous_param")
public class SubstanceHazardousParam implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String sectionNo;
    private String title;

    @OneToMany(mappedBy = "param")
    private List<SubstanceHazardousParamValue> values = new ArrayList<>();
}

