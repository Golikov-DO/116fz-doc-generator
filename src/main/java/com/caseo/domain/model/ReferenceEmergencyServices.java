package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "emergency_services")
public class ReferenceEmergencyServices implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String serviceName;

    @Column(name = "position")
    private String positionContact;

    @Column(name = "phone_number")
    private String phone;
    private String address;
}
