export interface HazardousParamValue {
    paramId: number
    sectionNo: string
    title: string
    valueText: string
    sourceInfo: string
}

export interface HazardousSubstanceFull {
    id: number
    name: string
    nameShort: string
    values: HazardousParamValue[]
}