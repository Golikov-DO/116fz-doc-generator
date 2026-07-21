import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getScenarios, deleteScenario, type Scenario } from "../../api/scenarioApi"
import ScenarioTable from "../../components/tables/ScenarioTable"

export default function ScenariosPage() {
    const navigate = useNavigate()
    const [scenarios, setScenarios] = useState<Scenario[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getScenarios()
            })
            .then(data => {
                if (data) setScenarios(data)
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить сценарий?')) return
        try {
            await deleteScenario(id)
            setScenarios(scenarios.filter(s => s.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <ScenarioTable
            scenarios={scenarios}
            onDelete={handleDelete}
        />
    )
}