package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "organization")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "organization_signer")
public class OrganizationSigner implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "signer_surname_basic")
    private String name;

    @Column(name = "signer_position")
    private String position;
}