import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getCities, deleteCity, type City } from "../../api/cityApi"
import CityTable from "../../components/tables/CityTable"

export default function CitiesPage() {
    const navigate = useNavigate()
    const [cities, setCities] = useState<City[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getCities()
            })
            .then(data => {
                if (data)
                {
                    const sorted = data.sort((a, b) =>
                        a.cityName.localeCompare(b.cityName, 'ru')
                    )
                    setCities(sorted)
                }
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить регион?')) return
        try {
            await deleteCity(id)
            setCities(cities.filter(c => c.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <CityTable
            cities={cities}
            onDelete={handleDelete}
        />
    )
}