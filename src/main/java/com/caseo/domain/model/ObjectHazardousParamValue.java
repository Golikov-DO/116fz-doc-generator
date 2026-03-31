package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "param")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "hazardous_param_value")
public class ObjectHazardousParamValue implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "param_id")
    private ObjectHazardousParam param;

    private String valueText;
    private String sourceInfo;
}

