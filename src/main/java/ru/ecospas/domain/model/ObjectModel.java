package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private ReferenceType type;

    @ManyToOne
    @JoinColumn(name = "hazardous_substance_id")
    private ReferenceHazardousSubstance hazardousSubstance;

    @OneToOne(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private ObjectOrderMinimumBalance minimumBalance;

    @OneToOne(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private ObjectAddress address;

    @OneToOne(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private ObjectInsurancePolicy insurancePolicy;

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectPersonsResponsible> responsiblePersons = new ArrayList<>();

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectCompositionKchs> compositionKchs = new ArrayList<>();

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectFireEquipment> fireEquipments = new ArrayList<>();

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectImage> images = new ArrayList<>();

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectStructure> structures = new ArrayList<>();

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectTechnologicalBlock> technologicalBlocks = new ArrayList<>();

    @OneToMany(
            mappedBy = "object",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ObjectTechnologicalEquipment> technologicalEquipments = new ArrayList<>();

    private Integer asfSignerId;
    private int hazardClass;

    @Column(name = "full_name")
    private String objectFullName;
    private String amountOfHazardousSubstance;
    private String nearestFireStation;

    @Column(name = "department_gochs_city")
    private String departmentGoChsCity;
    private boolean emergencyCommission;

    // Arrival time of the ASF unit
    @Column(columnDefinition = "time")
    private LocalTime arrivalTime;
}