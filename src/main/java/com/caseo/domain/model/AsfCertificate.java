package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_certificate")
public class AsfCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    private String certNumber;
    private String certSeries;
    private String issuedBy;
    private String issueBasis;
    @Column(columnDefinition = "date")
    private String issueDate;
    @Column(columnDefinition = "date")
    private String validUntil;

    public AsfCertificate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Asf getAsf() {
        return asf;
    }

    public void setAsf(Asf asf) {
        this.asf = asf;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public String getCertSeries() {
        return certSeries;
    }

    public void setCertSeries(String certSeries) {
        this.certSeries = certSeries;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssueBasis() {
        return issueBasis;
    }

    public void setIssueBasis(String issueBasis) {
        this.issueBasis = issueBasis;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
