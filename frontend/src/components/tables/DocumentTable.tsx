import { Eye, X } from "lucide-react"
import type { Document } from "../../types/document"
import { th, td, iconBtn } from "./tableStyles"

interface Props {
    documents: Document[]
    onOpen: (document: Document) => void
    onDelete: (document: Document) => void
}

function formatSize(size: number) {
    return `${(size / 1024 / 1024).toFixed(1)} МБ`
}

export default function DocumentTable({documents, onOpen, onDelete}: Props) {

    return (
        <div
            style={{
                background: "#fff",
                border: "1px solid #ddd",
                borderTop: "3px solid #4caf50",
                borderRadius: "4px",
                padding: "24px",
            }}
        >
            <h2 style={{margin: "0 0 20px", fontSize: "22px", fontWeight: 700, color: "#1a1a1a",}}>
                Разработанные документы
            </h2>

            <table style={{width: "100%", borderCollapse: "collapse",}}>
                <thead>
                <tr style={{ background: "#f5f5f5" }}>
                    <th style={{ ...th, width: "60px", textAlign: "center" }}>№</th>
                    <th style={th}>Организация</th>
                    <th style={th}>Документ</th>
                    <th style={{ ...th, width: "120px" }}>Размер</th>
                    <th style={{ ...th, width: "120px" }}>Действия</th>
                </tr>
                </thead>
                <tbody>
                {documents.length > 0 ? (
                    documents.map((document, index) => (
                        <tr key={`${document.organizationName}-${document.fileName}`}>
                            <td style={{ ...td, textAlign: "center" }}>
                                {index + 1}
                            </td>
                            <td style={td}>
                                {document.organizationName}
                            </td>
                            <td style={td}>
                                {document.fileName}
                            </td>
                            <td style={td}>
                                {formatSize(document.size)}
                            </td>
                            <td style={td}>
                                <div style={{ display: "flex", gap: "8px" }}>
                                    <button
                                        title="Просмотр"
                                        style={iconBtn}
                                        onClick={() => onOpen(document)}
                                    >
                                        <Eye size={16}/>
                                    </button>
                                    <button
                                        title="Удалить"
                                        style={{...iconBtn, border: "none", color: "#999"}}
                                        onClick={() => onDelete(document)}
                                    >
                                        <X size={16}/>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={5} style={{...td, textAlign: "center", color: "#666"}}>
                            Документы не найдены
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}