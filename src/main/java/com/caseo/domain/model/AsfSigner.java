package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_signer")
public class AsfSigner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    @Column(name = "signer_name")
    private String name;

    @Column(name = "signer_position")
    private String position;

    public AsfSigner() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Asf getAsf() { return asf; }
    public void setAsf(Asf asf) { this.asf = asf; }

    // Для совместимости со старым кодом
    public int getAsfId() {
        return asf != null ? asf.getId() : 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}