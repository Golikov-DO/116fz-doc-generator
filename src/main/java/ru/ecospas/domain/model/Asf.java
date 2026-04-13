package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf")
public class Asf implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String fullName;
    // Full name in genitive case (used in document templates)
    private String fullNameGen;
    private String shortName;
    private String statusShort;

    // Arrival time of the ASF unit
    @Column(columnDefinition = "time")
    private LocalTime arrivalTime;
}