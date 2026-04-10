package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "organization")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "organization_contact")
public class OrganizationContact implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private String fullName;
    private String position;
    private String phones;
    private String address;
}
