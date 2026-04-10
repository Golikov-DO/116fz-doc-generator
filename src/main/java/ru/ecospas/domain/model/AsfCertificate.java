package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_certificate")
public class AsfCertificate implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private String certNumber;
    private String certSeries;
    private String issuedBy;
    private String issueBasis;
    private LocalDate issueDate;
    private LocalDate validUntil;
}
