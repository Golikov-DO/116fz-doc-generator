package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table (name = "table_title")
public class ReferenceTableTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "table_linc")
    private String tableTextLinc;

    @Column(name = "table_name")
    private String tableTextName;

    public ReferenceTableTitle() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableTextLinc() {
        return tableTextLinc;
    }

    public void setTableTextLinc(String tableTextLinc) {
        this.tableTextLinc = tableTextLinc;
    }

    public String getTableTextName() {
        return tableTextName;
    }

    public void setTableTextName(String tableTextName) {
        this.tableTextName = tableTextName;
    }
}
