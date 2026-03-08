package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_personnel")
public class AsfPersonnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private int staffByStaffing;
    private int staffByList;
    private int certifiedTotal;
    private int qualifiedTotal;
    private int firstClass;
    private int thirdClass;
    private int secondClass;
    private int internationalClass;

    public AsfPersonnel() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Asf getAsf() { return asf; }
    public void setAsf(Asf asf) { this.asf = asf; }

    public int getStaffByStaffing() { return staffByStaffing; }
    public void setStaffByStaffing(int staffByStaffing) { this.staffByStaffing = staffByStaffing; }

    public int getStaffByList() { return staffByList; }
    public void setStaffByList(int staffByList) { this.staffByList = staffByList; }

    public int getCertifiedTotal() { return certifiedTotal; }
    public void setCertifiedTotal(int certifiedTotal) { this.certifiedTotal = certifiedTotal; }

    public int getQualifiedTotal() { return qualifiedTotal; }
    public void setQualifiedTotal(int qualifiedTotal) { this.qualifiedTotal = qualifiedTotal; }

    public int getThirdClass() { return thirdClass; }
    public void setThirdClass(int thirdClass) { this.thirdClass = thirdClass; }

    public int getSecondClass() { return secondClass; }
    public void setSecondClass(int secondClass) { this.secondClass = secondClass; }

    public int getFirstClass() { return firstClass; }
    public void setFirstClass(int firstClass) { this.firstClass = firstClass; }

    public int getInternationalClass() { return internationalClass; }
    public void setInternationalClass(int internationalClass) { this.internationalClass = internationalClass; }
}