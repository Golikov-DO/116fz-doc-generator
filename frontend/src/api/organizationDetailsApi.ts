import api from "./api";

export interface Organization {

    id: number;
    organizationName: string;
    organizationShortName: string;
    organizationTypeActivity: string;
    oneTerritory: boolean;
}

export async function getOrganization(id: number) {

    const response = await api.get("/api/organizations/" + id);

    return response.data as Organization;
}

export async function updateOrganization(organization: Organization) {

    const response = await api.put(
        "/api/organizations/" + organization.id,
        organization
    );

    return response.data as Organization;
}