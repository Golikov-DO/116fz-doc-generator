package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "object")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object_insurance_policy")
public class ObjectInsurancePolicy implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private String number;
    private LocalDate validUntil;
}