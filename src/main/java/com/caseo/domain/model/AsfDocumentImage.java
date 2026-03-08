package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_document_image")
public class AsfDocumentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String groupKey;
    @Column(columnDefinition = "bytea")
    private byte[] imageBlob;
    private String nameDocument;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    public AsfDocumentImage() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getNameDocument() {
        return nameDocument;
    }

    public void setNameDocument(String nameDocument) {
        this.nameDocument = nameDocument;
    }

    public Asf getAsf() {
        return asf;
    }

    public void setAsf(Asf asf) {
        this.asf = asf;
    }
}
