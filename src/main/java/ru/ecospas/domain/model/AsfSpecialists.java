package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_specialists")
public class AsfSpecialists implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private int totalCount;
    private int asrTp;
    private int asrLrnTer;
    private int gzsr;
    private int psr;
    private int driver;
    private int asrLrnSea;
}