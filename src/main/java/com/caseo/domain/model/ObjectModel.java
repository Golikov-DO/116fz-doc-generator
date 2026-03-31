package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"organization", "asf", "city", "type", "hazardousSubstance"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object")
public class ObjectModel implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    @ManyToOne
    @JoinColumn(name = "object_city_id")
    private ReferenceCity city;

    @ManyToOne
    @JoinColumn(name = "object_type_id")
    private ObjectType type;

    @ManyToOne
    @JoinColumn(name = "hazardous_substance_id")
    private ObjectHazardousSubstance hazardousSubstance;

    private int asfSignerId;
    private int hazardClass;

    @Column(name = "full_name")
    private String objectFullName;
    private String amountOfHazardousSubstance;
    private String nearestFireStation;

    @Column(name = "department_gochs_city")
    private String departmentGoChsCity;
    private boolean emergencyCommission;
}