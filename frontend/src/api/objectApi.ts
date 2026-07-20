// src/api/objectApi.ts
import type {
    ObjectFull,
    TypeItem, CityItem, AsfItem, AsfSigner
} from '../types/object'

export interface ObjectModel {
    id: number;
    objectFullName: string;
}

export interface ObjectWithOrg {
    id: number;
    objectFullName: string;
    organizationId: number;
    organizationShortName: string;
}

export async function getAllObjects(): Promise<ObjectWithOrg[]> {
    const res = await fetch('/api/objects', { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load objects');
    return res.json();
}

export async function getObjectsByOrganization(orgId: number): Promise<ObjectModel[]> {
    const res = await fetch(`/api/organizations/${orgId}/objects`, { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load objects');
    return res.json();
}

// Получить один объект (полные данные)
export async function getObject(orgId: number, id: number): Promise<ObjectFull> {
    const res = await fetch(`/api/organizations/${orgId}/objects/${id}`, { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load object');
    return res.json();
}

// Создать объект
export async function createObject(orgId: number, data: ObjectFull): Promise<ObjectFull> {
    const res = await fetch(`/api/organizations/${orgId}/objects`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Failed to create object');
    return res.json();
}

// Обновить объект
export async function updateObject(orgId: number, id: number, data: ObjectFull): Promise<void> {
    const res = await fetch(`/api/organizations/${orgId}/objects/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Failed to save object');
}

// Удалить объект
export async function deleteObject(orgId: number, id: number): Promise<void> {
    const res = await fetch(`/api/organizations/${orgId}/objects/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Failed to delete object');
}

// Справочники
export async function getTypes(): Promise<TypeItem[]> {
    const res = await fetch('/api/types', { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load types');
    return res.json();
}

export async function getHazardousSubstances(): Promise<TypeItem[]> {
    const res = await fetch('/api/hazardous-substances', { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load substances');
    return res.json();
}

export async function getCities(): Promise<CityItem[]> {
    const res = await fetch('/api/cities', { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load cities');
    return res.json();
}

export async function getAsfList(): Promise<AsfItem[]> {
    const res = await fetch('/api/asfs', { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load ASF');
    return res.json();
}

export async function getAsfWithSigners(asfId: number): Promise<{ signers: AsfSigner[] }> {
    const res = await fetch(`/api/asfs/${asfId}`, { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to load ASF');
    return res.json();
}

export async function setPrimarySigner(asfId: number, signerId: number, isPrimary: boolean): Promise<void> {
    const res = await fetch(`/api/asfs/${asfId}/signers/${signerId}/primary`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(isPrimary),
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Failed to update primary signer');
}