package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_personnel")
public class AsfPersonnel implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private int staffByStaffing;
    private int staffByList;
    private int certifiedTotal;
    private int qualifiedTotal;
    private int firstClass;
    private int thirdClass;
    private int secondClass;
    private int internationalClass;
}