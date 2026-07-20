import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {Typography} from '@mui/material';
import {getAllObjects, type ObjectWithOrg} from '../../api/objectApi.ts';
import {Eye, Pen, X} from "lucide-react";
import {th, td, iconBtn} from './tableStyles'

export default function AllObjectsTable() {
    const navigate = useNavigate();
    const [objects, setObjects] = useState<ObjectWithOrg[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getAllObjects()
            .then(setObjects)
            .catch(() => alert('Ошибка загрузки'))
            .finally(() => setLoading(false));
    }, []);

    const handleDelete = async (organizationId: number, id: number) => {
        if (!confirm("Удалить объект?")) return;

        try {
            await fetch(`/api/organizations/${organizationId}/objects/${id}`, {
                method: "DELETE",
                credentials: "include",
            });

            setObjects(objects.filter(o => o.id !== id));
        } catch {
            alert("Ошибка удаления");
        }
    };

    if (loading) return <Typography>Загрузка...</Typography>;

    return (
        <div style={{
            background: "#fff",
            border: "1px solid #ddd",
            borderTop: "3px solid #4caf50",
            borderRadius: "4px",
            padding: "24px",
        }}>
            <h2 style={{margin: "0 0 20px", fontSize: "22px", fontWeight: 700, color: "#1a1a1a"}}>
                Список объектов
            </h2>

            <table style={{width: "100%", borderCollapse: "collapse"}}>
                <thead>
                <tr style={{background: "#f5f5f5"}}>
                    <th style={th}>№</th>
                    <th style={th}>Организация</th>
                    <th style={th}>Наименование объекта</th>
                    <th style={{...th, width: "200px"}}>Действия</th>
                </tr>
                </thead>
                <tbody>
                {objects.length > 0 ? (
                    objects.map((obj, index) => (
                        <tr key={obj.id}>
                            <td style={{...td, textAlign: "center"}}>{index + 1}</td>
                            <td style={td}>{obj.organizationShortName}</td>
                            <td style={td}>{obj.objectFullName}</td>
                            <td style={td}>
                                <div style={{display: "flex", gap: "8px"}}>
                                    <button
                                        title="Просмотр"
                                        style={iconBtn}
                                        onClick={() =>
                                            navigate(`/organizations/${obj.organizationId}/objects/${obj.id}`)}
                                    >
                                        <Eye size={16}/>
                                    </button>
                                    <button
                                        title="Редактировать"
                                        style={iconBtn}
                                        onClick={() =>
                                            navigate(`/organizations/${obj.organizationId}/objects/${obj.id}/edit`)}
                                    >
                                        <Pen size={16}/>
                                    </button>
                                    <button style={{...iconBtn, color: "#999"}} onClick={() =>
                                        handleDelete(obj.organizationId, obj.id)}
                                    >
                                        <X size={16}/>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={4} style={{...td, textAlign: "center", color: "#666"}}>
                            Объектов не найдено
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}