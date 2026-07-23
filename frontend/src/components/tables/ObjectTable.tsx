import {useNavigate} from "react-router-dom"
import {Eye, Pen, FileText, Download, X} from "lucide-react"
import type {ObjectModel} from "../../api/objectApi"
import {th, td, iconBtn, addBtn} from './tableStyles'

interface Props {
    objects: ObjectModel[]
    orgId: number
    onDelete: (id: number) => void
}

export default function ObjectTable({objects, orgId, onDelete}: Props) {
    const navigate = useNavigate()

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
            <h2 style={{margin: "0 0 20px", fontSize: "22px", fontWeight: 700, color: "#1a1a1a"}}>
                Список объектов
            </h2>

            <table style={{width: "100%", borderCollapse: "collapse"}}>
                <thead>
                <tr style={{background: "#f5f5f5"}}>
                    <th style={{...th, width: "60px", textAlign: "center"}}>№</th>
                    <th style={th}>Наименование объекта</th>
                    <th style={{...th, width: "280px"}}>Действия</th>
                </tr>
                </thead>
                <tbody>
                {objects.length > 0 ? (
                    objects.map((obj, index) => (
                        <tr key={obj.id}>
                            <td style={{...td, textAlign: "center"}}>{index + 1}</td>
                            <td style={td}>{obj.objectFullName}</td>
                            <td style={td}>
                                <div style={{display: "flex", gap: "8px"}}>
                                    <button
                                        title="Просмотр"
                                        style={iconBtn}
                                        onClick={() =>
                                            navigate(`/organizations/${orgId}/objects/${obj.id}`)}
                                    >
                                        <Eye size={16}/>
                                    </button>
                                    <button
                                        title="Редактировать"
                                        style={iconBtn}
                                        onClick={() =>
                                            navigate(`/organizations/${orgId}/objects/${obj.id}/edit`)}
                                    >
                                        <Pen size={16}/>
                                    </button>
                                    <button
                                        title="Разработать план"
                                        style={iconBtn}
                                        onClick={async () => {
                                            try {
                                                const res = await fetch(`/api/plans/${obj.id}/generate`, {
                                                    method: 'POST',
                                                    credentials: 'include'
                                                })
                                                if (!res.ok) {
                                                    const text = await res.text()
                                                    alert(text)
                                                }
                                                alert('План успешно сгенерирован')
                                            } catch (e: any) {
                                                alert('Ошибка генерации: ' + e.message)
                                            }
                                        }}
                                    >
                                        <FileText size={16}/>
                                    </button>

                                    <button
                                        title="Скачать план"
                                        style={iconBtn}
                                        onClick={() => {
                                            window.open(`/api/plans/${obj.id}/download`, '_blank')
                                        }}
                                    >
                                        <Download size={16}/>
                                    </button>
                                    <button
                                        title="Удалить"
                                        style={{...iconBtn, border: "none", color: "#999"}}
                                        onClick={() => onDelete(obj.id)}
                                    >
                                        <X size={16}/>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={3} style={{...td, textAlign: "center", color: "#666"}}>
                            Объектов не найдено
                        </td>
                    </tr>
                )}
                </tbody>
            </table>

            <button style={addBtn} onClick={() => navigate(`/organizations/${orgId}/objects/new`)}>
                Добавить объект
            </button>
        </div>
    )
}