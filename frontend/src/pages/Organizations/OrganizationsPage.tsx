import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getOrganizations, deleteOrganization, type Organization } from "../../api/organizationApi"
import OrganizationTable from "../../components/tables/OrganizationTable"

export default function OrganizationsPage() {
    const navigate = useNavigate()
    const [organizations, setOrganizations] = useState<Organization[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getOrganizations()
            })
            .then(data => {
                if (data) setOrganizations(data)
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить организацию?')) return
        try {
            await deleteOrganization(id)
            setOrganizations(organizations.filter(o => o.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <OrganizationTable
            organizations={organizations}
            onDelete={handleDelete}
        />
    )
}