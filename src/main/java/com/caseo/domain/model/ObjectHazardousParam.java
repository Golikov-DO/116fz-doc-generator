package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"substance","values"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "hazardous_param")
public class ObjectHazardousParam implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substance_id")
    private ObjectHazardousSubstance substance;

    private String sectionNo;
    private String title;
    private String subtitle;

    @OneToMany(mappedBy = "param", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectHazardousParamValue> values = new ArrayList<>();

    public void addValue(ObjectHazardousParamValue value) {
        values.add(value);
        value.setParam(this);
    }

    public void removeValue(ObjectHazardousParamValue value) {
        values.remove(value);
        value.setParam(null);
    }
}

