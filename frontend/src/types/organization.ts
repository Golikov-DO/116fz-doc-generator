export interface OrganizationFull {
    id?: number
    organizationName: string
    organizationShortName: string
    organizationTypeActivity: string
    oneTerritory: boolean
    address: {
        addressIndex: string
        constituentEntity: string
        city: string
        street: string
        house: string
    } | null
    signers: Array<{
        id?: number
        name: string
        position: string
        isPrimary?: boolean
    }>
    contacts: Array<{
        id?: number
        fullName: string
        position: string
        phones: string
        address: string
    }>
}