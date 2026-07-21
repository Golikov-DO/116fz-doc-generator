import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getEmergencyServices, deleteEmergencyService, type EmergencyService } from "../../api/emergencyServiceApi"
import EmergencyServiceTable from "../../components/tables/EmergencyServiceTable"

export default function EmergencyServicesPage() {
    const navigate = useNavigate()
    const [emergencyServices, setEmergencyServices] = useState<EmergencyService[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getEmergencyServices()
            })
            .then(data => {
                if (data) {
                    const sorted = data.sort((a, b) =>
                        a.serviceName.localeCompare(b.serviceName, 'ru')
                    )
                    setEmergencyServices(sorted)
                }
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить экстренную службу?')) return
        try {
            await deleteEmergencyService(id)
            setEmergencyServices(prev => prev.filter(s => s.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <EmergencyServiceTable
            emergencyServices={emergencyServices}
            onDelete={handleDelete}
        />
    )
}