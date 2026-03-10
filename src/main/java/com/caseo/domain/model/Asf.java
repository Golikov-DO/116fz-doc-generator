package com.caseo.domain.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "asf")
public class Asf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;
    private String fullNameGen;
    private String shortName;
    private String email;
    private String statusShort;
    private LocalTime arrivalTime;

    public Asf() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameGen() {
        return fullNameGen;
    }

    public void setFullNameGen(String fullNameGen) {
        this.fullNameGen = fullNameGen;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatusShort() {
        return statusShort;
    }

    public void setStatusShort(String statusShort) {
        this.statusShort = statusShort;
    }

    @Column(columnDefinition = "time")
    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}