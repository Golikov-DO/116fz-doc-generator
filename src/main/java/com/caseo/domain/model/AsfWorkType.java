package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_work_type")
public class AsfWorkType implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private String name;
}