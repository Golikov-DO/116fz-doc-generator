import type { EmergencyServiceFull } from '../types/emergencyService'

export interface EmergencyService {
    id: number
    serviceName: string
}

export async function getEmergencyServices(): Promise<EmergencyService[]> {
    const res = await fetch('/api/emergency-services', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load emergency services')
    return res.json()
}

export async function getEmergencyService(id: number): Promise<EmergencyServiceFull> {
    const res = await fetch(`/api/emergency-services/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load emergency service')
    return res.json()
}

export async function updateEmergencyService(id: number, data: EmergencyServiceFull): Promise<void> {
    const res = await fetch(`/api/emergency-services/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save emergency service')
}

export async function createEmergencyService(data: EmergencyServiceFull): Promise<EmergencyService> {
    const res = await fetch('/api/emergency-services', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create emergency service')
    return res.json()
}

export async function deleteEmergencyService(id: number): Promise<void> {
    const res = await fetch(`/api/emergency-services/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}