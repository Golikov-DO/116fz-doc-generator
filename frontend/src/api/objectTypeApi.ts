import type { ReferenceTypeFull } from '../types/objectType'

// Тип для списка
export interface ReferenceType {
    id: number
    type: string
}

// Получить список типов объектов
export async function getObjectTypes(): Promise<ReferenceType[]> {
    const res = await fetch('/api/types', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load object types')
    return res.json()
}

// Получить один тип объекта (полные данные)
export async function getObjectType(id: number): Promise<ReferenceTypeFull> {
    const res = await fetch(`/api/types/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load object type')
    return res.json()
}

// Обновить тип объекта
export async function updateObjectType(id: number, data: ReferenceTypeFull): Promise<void> {
    const res = await fetch(`/api/types/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save object type')
}

// Создать тип объекта
export async function createObjectType(data: ReferenceTypeFull): Promise<ReferenceType> {
    const res = await fetch('/api/types', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create object type')
    return res.json()
}

// Удалить тип объекта
export async function deleteObjectType(id: number): Promise<void> {
    const res = await fetch(`/api/types/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}