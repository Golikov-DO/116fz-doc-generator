package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_composition_deployment_funds")
public class AsfCompositionDeploymentFunds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    // Поля без @Column - Hibernate сам сопоставит с таблицей
    private String responsibilityArea;
    private String deploymentPlace;
    private String dutyOfficerTelephone;
    private String contactTelephone;
    private String eMail;
    private String numberBuildings;
    private String totalArea;

    public AsfCompositionDeploymentFunds() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Asf getAsf() { return asf; }
    public void setAsf(Asf asf) { this.asf = asf; }

    public String getResponsibilityArea() { return responsibilityArea; }
    public void setResponsibilityArea(String responsibilityArea) { this.responsibilityArea = responsibilityArea; }

    public String getDeploymentPlace() { return deploymentPlace; }
    public void setDeploymentPlace(String deploymentPlace) { this.deploymentPlace = deploymentPlace; }

    public String getDutyOfficerTelephone() { return dutyOfficerTelephone; }
    public void setDutyOfficerTelephone(String dutyOfficerTelephone) { this.dutyOfficerTelephone = dutyOfficerTelephone; }

    public String getContactTelephone() { return contactTelephone; }
    public void setContactTelephone(String contactTelephone) { this.contactTelephone = contactTelephone; }

    public String getEMail() { return eMail; }
    public void setEMail(String eMail) { this.eMail = eMail; }

    public String getNumberBuildings() { return numberBuildings; }
    public void setNumberBuildings(String numberBuildings) { this.numberBuildings = numberBuildings; }

    public String getTotalArea() { return totalArea; }
    public void setTotalArea(String totalArea) { this.totalArea = totalArea; }
}