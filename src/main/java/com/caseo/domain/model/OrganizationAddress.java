package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "organization")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "organization_address")
public class OrganizationAddress implements Addressable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private Integer addressIndex;
    private String constituentEntity;
    private String areaHierarchy;

    @Column(name = "city_name")
    private String city;
    private String street;
    private String house;
    private String rawAddress;
}