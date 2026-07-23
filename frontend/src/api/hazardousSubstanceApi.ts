import type { HazardousSubstanceFull } from '../types/hazardousSubstance'

// Тип для списка
export interface HazardousSubstance {
    id: number
    name: string
}

export interface HazardousParam {
    id: number
    sectionNo: string
    title: string
}

// Получить список опасных веществ
export async function getHazardousSubstances(): Promise<HazardousSubstance[]> {
    const res = await fetch('/api/hazardous-substances', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load hazardous substances')
    return res.json()
}

// Получить одно вещество (полные данные)
export async function getHazardousSubstance(id: number): Promise<HazardousSubstanceFull> {
    const res = await fetch(`/api/hazardous-substances/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load hazardous substance')
    return res.json()
}

// Обновить вещество
export async function updateHazardousSubstance(id: number, data: HazardousSubstanceFull): Promise<void> {
    const res = await fetch(`/api/hazardous-substances/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save hazardous substance')
}

// Создать вещество
export async function createHazardousSubstance(data: HazardousSubstanceFull): Promise<HazardousSubstance> {
    const res = await fetch('/api/hazardous-substances', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create hazardous substance')
    return res.json()
}

// Удалить вещество
export async function deleteHazardousSubstance(id: number): Promise<void> {
    const res = await fetch(`/api/hazardous-substances/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}

//Получить параметры
export async function getHazardousParams(): Promise<HazardousParam[]> {
    const res = await fetch('/api/hazardous-substances/params', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load params')
    return res.json()
}