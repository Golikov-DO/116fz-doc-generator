export interface RegionalAuthority {
    id: number
    name: string
    department: string
    phoneNumber: string
    address: string
}

export interface CityFull {
    id: number
    geoRelief: string
    geoGeology: string
    climatDesc: string
    hydroDesc: string
    infraTransport: string
    infraEngineering: string
    infraOrganizations: string
    nearbyTowns: string
    massPeoplePlaces: string
    adminStatus: string
    distCenters: string
    cityName: string
    regionalAuthorities: RegionalAuthority[]
}