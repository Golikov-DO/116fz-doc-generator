import type { TableTitleFull } from '../types/tableTitle'

export interface TableTitle {
    id: number
    tableTextLinc: string
    tableTextName: string
}

export async function getTableTitles(): Promise<TableTitle[]> {
    const res = await fetch('/api/table-titles', { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load table titles')
    return res.json()
}

export async function getTableTitle(id: number): Promise<TableTitleFull> {
    const res = await fetch(`/api/table-titles/${id}`, { credentials: 'include' })
    if (!res.ok) throw new Error('Failed to load table title')
    return res.json()
}

export async function updateTableTitle(id: number, data: TableTitleFull): Promise<void> {
    const res = await fetch(`/api/table-titles/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to save table title')
}

export async function createTableTitle(data: TableTitleFull): Promise<TableTitle> {
    const res = await fetch('/api/table-titles', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to create table title')
    return res.json()
}

export async function deleteTableTitle(id: number): Promise<void> {
    const res = await fetch(`/api/table-titles/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    })
    if (!res.ok) throw new Error('Failed to delete')
}