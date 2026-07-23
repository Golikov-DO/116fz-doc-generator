package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "object")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object_responsible_persons")
public class ObjectPersonsResponsible implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private int number;
    private String fullName;
    private String position;
}
