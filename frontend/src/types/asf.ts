export interface Asf {
    id: number
    shortName: string
}

export interface AsfFull {
    id?: number
    fullName: string
    fullNameGen: string
    shortName: string
    statusShort: string
    certificate: {
        certNumber: string
        certSeries: string
        issuedBy: string
        issueBasis: string
        issueDate: string | null
        validUntil: string | null
    } | null
    personnel: {
        staffByStaffing: number
        staffByList: number
        certifiedTotal: number
        qualifiedTotal: number
        firstClass: number
        secondClass: number
        thirdClass: number
        internationalClass: number
    } | null
    specialists: {
        totalCount: number
        asrTp: number
        asrLrnTer: number
        gzsr: number
        psr: number
        driver: number
        asrLrnSea: number
    } | null
    deployment: {
        responsibilityArea: string
        deploymentPlace: string
        dutyOfficerTelephone: string
        contactTelephone: string
        eMail: string
        numberBuildings: string
        totalArea: string
    } | null
    signers: Array<{
        id?: number
        name: string
        position: string
        isPrimary?: boolean
    }>
    workTypes: Array<{
        id?: number
        name: string
    }>
    images: Array<{
        id?: number
        groupKey: string
        nameDocument: string
    }>
}