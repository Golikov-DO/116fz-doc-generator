import type { AsfFull } from '../types/asf'

// Тип для списка
export interface Asf {
    id: number
    shortName: string
}

// Получить список АСФ
export async function getAsfs(): Promise<Asf[]> {
    const res = await fetch('/api/asfs', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load ASFs')
    return res.json()
}

// Получить одну АСФ (полные данные)
export async function getAsf(id: number): Promise<AsfFull> {
    const res = await fetch(`/api/asfs/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load ASF')
    return res.json()
}

// Обновить АСФ
export async function updateAsf(id: number, data: AsfFull): Promise<void> {
    const res = await fetch(`/api/asfs/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save ASF')
}

// Создать АСФ
export async function createAsf(data: AsfFull): Promise<Asf> {
    const res = await fetch('/api/asfs', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create ASF')
    return res.json()
}

// Удалить АСФ
export async function deleteAsf(id: number): Promise<void> {
    const res = await fetch(`/api/asfs/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}