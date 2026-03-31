package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "params")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table (name = "hazardous_substance")
public class ObjectHazardousSubstance implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;
    private String nameGen;

    @OneToMany(mappedBy = "substance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectHazardousParam> params = new ArrayList<>();

    public void addParam(ObjectHazardousParam param) {
        params.add(param);
        param.setSubstance(this);
    }

    public void removeParam(ObjectHazardousParam param) {
        params.remove(param);
        param.setSubstance(null);
    }
}
