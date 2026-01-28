package com.caseo.domain.model;

public class AsfCertificate {

    private final int id;
    private final String certNumber;
    private final String certSeries;
    private final String issuedBy;
    private final String issueBasis;
    private final String issueDate;
    private final String validUntil;

    public AsfCertificate(
            int id,
            String certNumber,
            String certSeries,
            String issuedBy,
            String issueBasis,
            String issueDate,
            String validUntil
    ) {
        this.id = id;
        this.certNumber = certNumber;
        this.certSeries = certSeries;
        this.issuedBy = issuedBy;
        this.issueBasis = issueBasis;
        this.issueDate = issueDate;
        this.validUntil = validUntil;
    }

    public int getId() { return id; }
    public String getCertNumber() { return certNumber; }
    public String getCertSeries() { return certSeries; }
    public String getIssuedBy() { return issuedBy; }
    public String getIssueBasis() { return issueBasis; }
    public String getIssueDate() { return issueDate; }
    public String getValidUntil() { return validUntil; }
}
