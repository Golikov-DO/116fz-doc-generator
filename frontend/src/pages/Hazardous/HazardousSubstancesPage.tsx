import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getHazardousSubstances, deleteHazardousSubstance, type HazardousSubstance } from "../../api/hazardousSubstanceApi"
import HazardousSubstanceTable from "../../components/tables/HazardousSubstanceTable"

export default function HazardousSubstancesPage() {
    const navigate = useNavigate()
    const [substances, setSubstances] = useState<HazardousSubstance[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getHazardousSubstances()
            })
            .then(data => {
                if (data) setSubstances(data)
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить опасное вещество?')) return
        try {
            await deleteHazardousSubstance(id)
            setSubstances(substances.filter(s => s.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <HazardousSubstanceTable
            substances={substances}
            onDelete={handleDelete}
        />
    )
}