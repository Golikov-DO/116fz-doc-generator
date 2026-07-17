import type { OrganizationFull } from '../types/organization'

// Тип для списка (если используется в OrganizationsPage)
export interface Organization {
    id: number
    organizationShortName: string
    organizationName: string
}

// Получить список организаций
export async function getOrganizations(): Promise<Organization[]> {
    const res = await fetch('/api/organizations', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load organizations')
    return res.json()
}

// Получить одну организацию (полные данные)
export async function getOrganization(id: number): Promise<OrganizationFull> {
    const res = await fetch(`/api/organizations/${id}/full`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load organization')
    return res.json()
}

// Обновить организацию
export async function updateOrganization(id: number, data: OrganizationFull): Promise<void> {
    const res = await fetch(`/api/organizations/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save organization')
}

export async function deleteOrganization(id: number): Promise<void> {
    const res = await fetch(`/api/organizations/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}