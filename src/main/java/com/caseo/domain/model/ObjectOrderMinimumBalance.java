package com.caseo.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "object_order_minimum_balance")
public class ObjectOrderMinimumBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private String number;
    private LocalDate date;

    public ObjectOrderMinimumBalance() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}