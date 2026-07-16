package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "object_type")
public class ReferenceType implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "object_type_definitions")
    private String typeDefinition;
    private String type;

    @OneToMany(mappedBy = "type")
    private List<ObjectModel> objects = new ArrayList<>();
}