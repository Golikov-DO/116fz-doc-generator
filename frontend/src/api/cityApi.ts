import type { CityFull } from '../types/city'

// Тип для списка
export interface City {
    id: number
    cityName: string
}

export interface RegionalAuthority {
    id: number
    name: string
    department: string
    phoneNumber: string
    address: string
}

// Получить список городов
export async function getCities(): Promise<City[]> {
    const res = await fetch('/api/cities', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load cities')
    return res.json()
}

// Получить один город (полные данные)
export async function getCity(id: number): Promise<CityFull> {
    const res = await fetch(`/api/cities/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load city')
    return res.json()
}

// Обновить город
export async function updateCity(id: number, data: CityFull): Promise<void> {
    const res = await fetch(`/api/cities/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save city')
}

// Создать город
export async function createCity(data: CityFull): Promise<City> {
    const res = await fetch('/api/cities', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create city')
    return res.json()
}

// Удалить город
export async function deleteCity(id: number): Promise<void> {
    const res = await fetch(`/api/cities/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}