package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_image")
public class ObjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private String groupKey;
    @Column(columnDefinition = "bytea")
    private byte[] imageBlob;
    private String caption;
    private String linkText;

    public ObjectImage() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public String getGroupKey() { return groupKey; }
    public void setGroupKey(String groupKey) { this.groupKey = groupKey; }

    public byte[] getImageBlob() { return imageBlob; }
    public void setImageBlob(byte[] imageBlob) { this.imageBlob = imageBlob; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getLinkText() { return linkText; }
    public void setLinkText(String linkText) { this.linkText = linkText; }
}