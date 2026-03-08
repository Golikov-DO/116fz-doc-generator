package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_composition_kchs")
public class ObjectCompositionKchs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;  // вместо objectId

    private int number;
    private String position;
    private String fullName;
    private String workPhone;
    private String cellPhone;
    private String homeAddress;

    public ObjectCompositionKchs() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getWorkPhone() { return workPhone; }
    public void setWorkPhone(String workPhone) { this.workPhone = workPhone; }

    public String getCellPhone() { return cellPhone; }
    public void setCellPhone(String cellPhone) { this.cellPhone = cellPhone; }

    public String getHomeAddress() { return homeAddress; }
    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }
}