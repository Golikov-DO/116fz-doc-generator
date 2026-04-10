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
    private String fullNameGen;
    private String shortName;
    private String statusShort;

    @Column(columnDefinition = "time")
    private LocalTime arrivalTime;
}