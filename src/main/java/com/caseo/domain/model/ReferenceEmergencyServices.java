package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "emergency_services")
public class ReferenceEmergencyServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String serviceName;

    @Column(name = "position")
    private String positionContact;

    @Column(name = "phone_number")
    private String phone;
    private String address;

    public ReferenceEmergencyServices() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPositionContact() {
        return positionContact;
    }

    public void setPositionContact(String positionContact) {
        this.positionContact = positionContact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
