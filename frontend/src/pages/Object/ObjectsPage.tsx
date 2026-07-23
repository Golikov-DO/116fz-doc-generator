import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Typography } from '@mui/material';
import { getObjectsByOrganization, type ObjectModel } from '../../api/objectApi.ts';
import { getOrganization } from '../../api/organizationApi.ts';
import ObjectTable from '../../components/tables/ObjectTable.tsx';

export default function ObjectsPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const pathParts = location.pathname.split('/').filter(Boolean);
    const orgId = Number(pathParts[1]);

    const [objects, setObjects] = useState<ObjectModel[]>([]);
    const [orgName, setOrgName] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([
            getObjectsByOrganization(orgId),
            getOrganization(orgId)
        ])
            .then(([objectsData, orgData]) => {
                setObjects(objectsData);
                setOrgName(orgData.organizationShortName || '');
            })
            .catch(() => alert('Ошибка загрузки'))
            .finally(() => setLoading(false));
    }, [orgId]);

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить объект?')) return;
        try {
            await fetch(`/api/organizations/${orgId}/objects/${id}`, { method: 'DELETE', credentials: 'include' });
            setObjects(objects.filter(o => o.id !== id));
        } catch {
            alert('Ошибка удаления');
        }
    };

    if (loading) return <Typography>Загрузка...</Typography>;

    return (
        <div>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                Список объектов для {orgName}
            </Typography>

            <ObjectTable objects={objects} orgId={orgId} onDelete={handleDelete} />

            <div style={{ marginTop: "20px", textAlign: "center" }}>
                <button
                    style={{
                        padding: "10px 18px",
                        border: "1px solid #ccc",
                        borderRadius: "4px",
                        background: "#f0f0f0",
                        fontSize: "15px",
                        cursor: "pointer",
                    }}
                    onClick={() => navigate('/organizations')}
                >
                    Назад к организациям
                </button>
            </div>
        </div>
    );
}