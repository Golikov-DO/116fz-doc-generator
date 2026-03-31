package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_composition_deployment_funds")
public class AsfCompositionDeploymentFunds implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private String responsibilityArea;
    private String deploymentPlace;
    private String dutyOfficerTelephone;
    private String contactTelephone;
    private String eMail;
    private String numberBuildings;
    private String totalArea;
}