import { useNavigate } from "react-router-dom"
import { Eye, Pen, X } from "lucide-react"
import type { Scenario } from "../../api/scenarioApi"
import { th, td, iconBtn, addBtn } from './tableStyles.ts'

interface Props {
    scenarios: Scenario[]
    onDelete: (id: number) => void
}

export default function ScenarioTable({ scenarios, onDelete }: Props) {
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
                Список сценариев
            </h2>

            <table style={{ width: "100%", borderCollapse: "collapse" }}>
                <thead>
                <tr style={{ background: "#f5f5f5" }}>
                    <th style={{ ...th, width: "60px", textAlign: "center" }}>№</th>
                    <th style={th}>Наименование сценария</th>
                    <th style={{ ...th, width: "180px" }}>Действия</th>
                </tr>
                </thead>
                <tbody>
                {scenarios.length > 0 ? (
                    scenarios.map((scenario, index) => (
                        <tr key={scenario.id}>
                            <td style={{ ...td, textAlign: "center" }}>{index + 1}</td>
                            <td style={td}>{scenario.name ?? ""}</td>
                            <td style={td}>
                                <div style={{ display: "flex", gap: "8px" }}>
                                    <button
                                        title="Просмотр"
                                        style={iconBtn}
                                        onClick={() => navigate(`/scenarios/${scenario.id}`)}
                                    >
                                        <Eye size={16} />
                                    </button>
                                    <button
                                        title="Редактировать"
                                        style={iconBtn}
                                        onClick={() => navigate(`/scenarios/${scenario.id}/edit`)}
                                    >
                                        <Pen size={16} />
                                    </button>
                                    <button
                                        title="Удалить"
                                        style={{ ...iconBtn, border: "none", color: "#999" }}
                                        onClick={() => onDelete(scenario.id)}
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
                            Сценариев не найдено
                        </td>
                    </tr>
                )}
                </tbody>
            </table>

            <button style={addBtn} onClick={() => navigate("/scenarios/new", { state: { editMode: true } })}>
                Добавить сценарий
            </button>
        </div>
    )
}