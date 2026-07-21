import { useNavigate } from "react-router-dom"
import { Eye, Pen, X } from "lucide-react"
import type { ReferenceType } from "../../api/objectTypeApi"
import { th, td, iconBtn, addBtn } from './tableStyles.ts'

interface Props {
    objectTypes: ReferenceType[]
    onDelete: (id: number) => void
}

export default function ObjectTypeTable({ objectTypes, onDelete }: Props) {
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
            <h2 style={{ margin: "0 0 20px", fontSize: "22px", fontWeight: 700, color: "#1a1a1a" }}>
                Список типов объектов
            </h2>

            <table style={{ width: "100%", borderCollapse: "collapse" }}>
                <thead>
                <tr style={{ background: "#f5f5f5" }}>
                    <th style={{ ...th, width: "60px", textAlign: "center" }}>№</th>
                    <th style={th}>Наименование типа</th>
                    <th style={{ ...th, width: "180px" }}>Действия</th>
                </tr>
                </thead>
                <tbody>
                {objectTypes.length > 0 ? (
                    objectTypes.map((objectType, index) => (
                        <tr key={objectType.id}>
                            <td style={{ ...td, textAlign: "center" }}>{index + 1}</td>
                            <td style={td}>{objectType.type ?? ""}</td>
                            <td style={td}>
                                <div style={{ display: "flex", gap: "8px" }}>
                                    <button
                                        title="Просмотр"
                                        style={iconBtn}
                                        onClick={() => navigate(`/types/${objectType.id}`)}
                                    >
                                        <Eye size={16} />
                                    </button>
                                    <button
                                        title="Редактировать"
                                        style={iconBtn}
                                        onClick={() => navigate(`/types/${objectType.id}/edit`)}
                                    >
                                        <Pen size={16} />
                                    </button>
                                    <button
                                        title="Удалить"
                                        style={{ ...iconBtn, border: "none", color: "#999" }}
                                        onClick={() => onDelete(objectType.id)}
                                    >
                                        <X size={16} />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={3} style={{ ...td, textAlign: "center", color: "#666" }}>
                            Типов объектов не найдено
                        </td>
                    </tr>
                )}
                </tbody>
            </table>

            <button style={addBtn} onClick={() => navigate("/types/new", { state: { editMode: true } })}>
                Добавить тип объекта
            </button>
        </div>
    )
}