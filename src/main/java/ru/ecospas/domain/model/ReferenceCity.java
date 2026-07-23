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
@Table(name = "object_city")
public class ReferenceCity implements BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String geoRelief;
    private String geoGeology;
    private String climatDesc;
    private String hydroDesc;
    private String infraTransport;
    private String infraEngineering;
    private String infraOrganizations;
    private String nearbyTowns;
    private String massPeoplePlaces;
    private String adminStatus;
    private String distCenters;
    private String cityName;

    @OneToMany(mappedBy = "city")
    private List<ObjectModel> objects = new ArrayList<>();

    @OneToMany(
            mappedBy = "objectCity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CityRegionalAuthorities> regionalAuthorities = new ArrayList<>();
}