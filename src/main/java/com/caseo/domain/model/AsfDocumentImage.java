package com.caseo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "asf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "asf_document_image")
public class AsfDocumentImage implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String groupKey;
    @Column(columnDefinition = "bytea")
    private byte[] imageBlob;
    private String nameDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asf_id")
    private Asf asf;
}
