import type { Document } from "../types/document"

export async function getDocuments(): Promise<Document[]> {
    const res = await fetch("/api/documents", {
        credentials: "include"
    })

    if (!res.ok) {
        throw new Error("Failed to load documents")
    }

    return res.json()
}

export function openDocument(document: Document) {

    const organization = encodeURIComponent(document.organizationName)
    const file = encodeURIComponent(document.fileName)

    window.open(
        `/api/documents/download?organization=${organization}&file=${file}`,
        "_blank"
    )
}

export async function deleteDocument(document: Document) {

    const organization = encodeURIComponent(document.organizationName)
    const file = encodeURIComponent(document.fileName)

    const res = await fetch(
        `/api/documents?organization=${organization}&file=${file}`,
        {
            method: "DELETE",
            credentials: "include"
        }
    )

    if (!res.ok) {
        throw new Error(await res.text())
    }
}