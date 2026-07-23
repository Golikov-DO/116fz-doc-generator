import type { ScenarioFull } from '../types/scenario'

// Тип для списка
export interface Scenario {
    id: number
    name: string
}

// Получить список сценариев
export async function getScenarios(): Promise<Scenario[]> {
    const res = await fetch('/api/scenarios', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load scenarios')
    return res.json()
}

// Получить один сценарий (полные данные)
export async function getScenario(id: number): Promise<ScenarioFull> {
    const res = await fetch(`/api/scenarios/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load scenario')
    return res.json()
}

// Обновить сценарий
export async function updateScenario(id: number, data: ScenarioFull): Promise<void> {
    const res = await fetch(`/api/scenarios/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save scenario')
}

// Создать сценарий
export async function createScenario(data: ScenarioFull): Promise<Scenario> {
    const res = await fetch('/api/scenarios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create scenario')
    return res.json()
}

// Удалить сценарий
export async function deleteScenario(id: number): Promise<void> {
    const res = await fetch(`/api/scenarios/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}