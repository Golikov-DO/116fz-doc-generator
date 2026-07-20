import {useNavigate} from "react-router-dom"
import {Eye, Pen, Box, X} from "lucide-react"
import type {Organization} from "../../api/organizationApi"
import {th, td, iconBtn, addBtn} from './tableStyles'

interface Props {
    organizations: Organization[]
    onDelete: (id: number) => void
}

export default function OrganizationTable({organizations, onDelete}: Props) {
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
                Список организаций
            </h2>

            <table style={{width: "100%", borderCollapse: "collapse"}}>
                <thead>
                <tr style={{background: "#f5f5f5"}}>
                    <th style={{...th, width: "60px", textAlign: "center"}}>№</th>
                    <th style={th}>Наименование организации</th>
                    <th style={{...th, width: "220px"}}>Действия</th>
                </tr>
                </thead>
                <tbody>
                {organizations.length > 0 ? (
                    organizations.map((org, index) => (
                        <tr key={org.id}>
                            <td style={{...td, textAlign: "center"}}>{index + 1}</td>
                            <td style={td}>{org.organizationShortName ?? ""}</td>
                            <td style={td}>
                                <div style={{display: "flex", gap: "8px"}}>
                                    <button
                                        title="Просмотр"
                                        style={iconBtn}
                                        onClick={() => navigate(`/organization/${org.id}`)}
                                    >
                                        <Eye size={16}/>
                                    </button>
                                    <button
                                        title="Редактировать"
                                        style={iconBtn}
                                        onClick={() => navigate(`/organization/${org.id}/edit`)}
                                    >
                                        <Pen size={16}/>
                                    </button>
                                    <button title="Объекты"
                                            style={iconBtn}
                                            onClick={() => navigate(`/objects/${org.id}`)}>
                                        <Box size={16}/>
                                    </button>
                                    <button
                                        title="Удалить"
                                        style={{...iconBtn, border: "none", color: "#999"}}
                                        onClick={() => onDelete(org.id)}
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
                            Организаций не найдено
                        </td>
                    </tr>
                )}
                </tbody>
            </table>

            <button style={addBtn} onClick={() =>
                navigate("/organization/new", {state: {editMode: true}})}>
                Добавить организацию
            </button>
        </div>
    )
}