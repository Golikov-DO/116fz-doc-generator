package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "object")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object_address")
public class ObjectAddress implements Addressable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private Integer addressIndex;
    private String constituentEntity;
    private String areaHierarchy;

    @Column(name = "city_name")
    private String city;
    private String street;
    private String house;
    private String coordinates;
    private String rawAddress;
}

