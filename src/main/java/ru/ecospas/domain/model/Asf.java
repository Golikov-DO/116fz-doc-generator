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
@Table(name = "asf")
public class Asf implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String fullName;
    // Full name in genitive case (used in document templates)
    private String fullNameGen;
    private String shortName;
    private String statusShort;

    @OneToOne(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AsfCertificate certificate;

    @OneToOne(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AsfPersonnel personnel;

    @OneToOne(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AsfSpecialists specialists;

    @OneToOne(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AsfCompositionDeploymentFunds deployment;

    @OneToMany(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AsfSigner> signers = new ArrayList<>();

    @OneToMany(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AsfWorkType> workTypes = new ArrayList<>();

    @OneToMany(
            mappedBy = "asf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AsfDocumentImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "asf")
    private List<ObjectModel> objects = new ArrayList<>();
}