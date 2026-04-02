package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"param","substance"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "hazardous_param_value")
public class ObjectHazardousParamValue implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "param_id")
    private ReferenceHazardousParam param;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substance_id")
    private ReferenceHazardousSubstance substance;

    private String valueText;
    private String sourceInfo;
}

