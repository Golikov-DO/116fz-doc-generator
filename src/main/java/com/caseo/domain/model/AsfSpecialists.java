package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_specialists")
public class AsfSpecialists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private int totalCount;
    private int asrTp;
    private int asrLrnTer;
    private int gzsr;
    private int psr;
    private int driver;
    private int asrLrnSea;

    public AsfSpecialists() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Asf getAsf() { return asf; }
    public void setAsf(Asf asf) { this.asf = asf; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getAsrTp() { return asrTp; }
    public void setAsrTp(int asrTp) { this.asrTp = asrTp; }

    public int getAsrLrnTer() { return asrLrnTer; }
    public void setAsrLrnTer(int asrLrnTer) { this.asrLrnTer = asrLrnTer; }

    public int getGzsr() { return gzsr; }
    public void setGzsr(int gzsr) { this.gzsr = gzsr; }

    public int getPsr() { return psr; }
    public void setPsr(int psr) { this.psr = psr; }

    public int getDriver() { return driver; }
    public void setDriver(int driver) { this.driver = driver; }

    public int getAsrLrnSea() { return asrLrnSea; }
    public void setAsrLrnSea(int asrLrnSea) { this.asrLrnSea = asrLrnSea; }
}