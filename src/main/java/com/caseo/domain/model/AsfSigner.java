package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_signer")
public class AsfSigner implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asf_id")
    private Asf asf;

    @Column(name = "signer_name")
    private String name;

    @Column(name = "signer_position")
    private String position;
}