// src/types/object.ts

export interface ObjectAddress {
    addressIndex: number | string
    constituentEntity: string
    areaHierarchy: string
    city: string
    street: string
    house: string
    coordinates: string
}

export interface ObjectInsurancePolicy {
    number: string
    validUntil: string
}

export interface ObjectOrderMinimumBalance {
    number: string
    date: string
}

export interface ObjectCompositionKchs {
    id?: number
    number: number
    fullName: string
    position: string
    workPhone: string
    cellPhone: string
    homeAddress: string
}

export interface ObjectPersonsResponsible {
    id?: number
    number: number
    fullName: string
    position: string
}

export interface ObjectFireEquipment {
    id?: number
    number: number
    productName: string
    quantity: string
    location: string
}

export interface ObjectTechnologicalEquipment {
    id?: number
    num: number
    name: string
    characteristics: string
}

export interface ObjectTechnologicalBlock {
    id?: number
    num: number
    name: string
}

export interface ObjectStructure {
    id?: number
    num: number
    name: string
    scenarios: ObjectScenario[]
    likelyIds: string
    dangerousIds: string
    probableScenarioIds: number[]
    dangerousScenarioIds: number[]
}

export interface ObjectScenario {
    id: number
    name: string
}

export interface ObjectImage {
    id: number
    groupKey: string
    caption: string
    linkText: string
    imageUrl?: string
}

export interface ObjectFull {
    id?: number
    organizationId: number
    asfId: number | null
    cityId: number | null
    typeId: number | null
    hazardousSubstanceId: number | null
    asfSignerId: number | null
    hazardClass: number
    objectFullName: string
    amountOfHazardousSubstance: string
    nearestFireStation: string
    departmentGoChsCity: string
    emergencyCommission: boolean
    arrivalTime: string
    address: ObjectAddress
    insurancePolicy: ObjectInsurancePolicy
    minimumBalance: ObjectOrderMinimumBalance
    compositionKchs: ObjectCompositionKchs[]
    responsiblePersons: ObjectPersonsResponsible[]
    fireEquipments: ObjectFireEquipment[]
    technologicalEquipments: ObjectTechnologicalEquipment[]
    technologicalBlocks: ObjectTechnologicalBlock[]
    structures: ObjectStructure[]
    images: ObjectImage[]
}

// Справочники
export interface TypeItem {
    id: number
    type: string
}

export interface HazardousSubstanceItem {
    id: number
    name: string
}

export interface CityItem {
    id: number
    cityName: string
}

export interface AsfItem {
    id: number
    shortName: string
}

export interface AsfSigner {
    id: number
    name: string
    position: string
    isPrimary: boolean
}