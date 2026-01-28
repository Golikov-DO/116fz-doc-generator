package com.caseo.domain.model;

import java.util.List;

public class Asf {

    private final int id;

    private final String fullName;
    private final String shortName;
    private final String status;
    private final String responsibilityZone;
    private final String email;
    private final int buildingsCount;
    private final String notes;
    private final String locationText;
    private final int documentSetId;
    private final String statusShort;
    private final String statusGen;

    // связанные сущности
    private List<AsfWorkType> workTypes;
    private AsfCertificate certificate;

    public Asf(
            int id,
            String fullName,
            String shortName,
            String status,
            String responsibilityZone,
            String email,
            int buildingsCount,
            String notes,
            String locationText,
            int documentSetId,
            String statusShort,
            String statusGen
    ) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.status = status;
        this.responsibilityZone = responsibilityZone;
        this.email = email;
        this.buildingsCount = buildingsCount;
        this.notes = notes;
        this.locationText = locationText;
        this.documentSetId = documentSetId;
        this.statusShort = statusShort;
        this.statusGen = statusGen;
    }

    public int getId() { return id; }

    public String getFullName() { return fullName; }
    public String getShortName() { return shortName; }
    public String getStatus() { return status; }
    public String getResponsibilityZone() { return responsibilityZone; }
    public String getEmail() { return email; }
    public int getBuildingsCount() { return buildingsCount; }
    public String getNotes() { return notes; }
    public String getLocationText() { return locationText; }
    public int getDocumentSetId() { return documentSetId; }
    public String getStatusShort() { return statusShort; }
    public String getStatusGen() { return statusGen; }

    public List<AsfWorkType> getWorkTypes() { return workTypes; }
    public void setWorkTypes(List<AsfWorkType> workTypes) { this.workTypes = workTypes; }

    public AsfCertificate getCertificate() { return certificate; }
    public void setCertificate(AsfCertificate certificate) { this.certificate = certificate; }
}