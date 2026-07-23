package ru.ecospas.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "organization")
public class Organization implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "organization_full_name")
    private String organizationName;

    private String organizationShortName;
    private String organizationTypeActivity;
    private boolean oneTerritory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "organization")
    private List<ObjectModel> objects = new ArrayList<>();

    @OneToOne(
            mappedBy = "organization",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private OrganizationAddress address;

    @OneToMany(
            mappedBy = "organization",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrganizationSigner> signers = new ArrayList<>();

    @OneToMany(
            mappedBy = "organization",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrganizationContact> contacts = new ArrayList<>();
}
