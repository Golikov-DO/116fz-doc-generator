import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getAsfs, deleteAsf, type Asf } from "../../api/asfApi"
import AsfTable from "../../components/tables/AsfTable"

export default function AsfsPage() {
    const navigate = useNavigate()
    const [asfs, setAsfs] = useState<Asf[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getAsfs()
            })
            .then(data => {
                if (data) setAsfs(data)
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить АСФ?')) return
        try {
            await deleteAsf(id)
            setAsfs(asfs.filter(a => a.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <AsfTable
            asfs={asfs}
            onDelete={handleDelete}
        />
    )
}