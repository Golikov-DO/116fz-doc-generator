import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import {getDocuments, openDocument, deleteDocument} from "../../api/documentApi"
import type { Document } from "../../types/document"
import DocumentTable from "../../components/tables/DocumentTable"

export default function DocumentsPage() {
    const navigate = useNavigate()
    const [documents, setDocuments] = useState<Document[]>([])
    const [loading, setLoading] = useState(true)
    useEffect(() => {
       void load()
    }, [])
    async function load() {
        try {
            const data = await getDocuments()
            setDocuments(data)
        } catch {
            navigate("/login")
        } finally {
            setLoading(false)
        }
    }

    async function handleDelete(document: Document) {
        if (!confirm(`Удалить "${document.fileName}"?`)) {
            return
        }
        try {
            await deleteDocument(document)
            setDocuments(prev =>
                prev.filter(d =>
                    !(
                        d.organizationName === document.organizationName &&
                        d.fileName === document.fileName
                    )
                )
            )
        } catch (e: any) {
            alert(e.message)
        }
    }
    if (loading) {
        return <div>Загрузка...</div>
    }
    return (
        <DocumentTable
            documents={documents}
            onOpen={openDocument}
            onDelete={handleDelete}
        />
    )
}