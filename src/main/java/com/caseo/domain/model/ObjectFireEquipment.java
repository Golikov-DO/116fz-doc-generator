package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "object")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object_primary_fire_extinguishing_equipment")
public class ObjectFireEquipment implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    @Column(name = "num")
    private int number;

    @Column(name = "name_product")
    private String productName;

    private String quantity;
    private String location;
}