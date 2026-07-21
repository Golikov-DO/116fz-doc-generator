import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getTableTitles, deleteTableTitle, type TableTitle } from "../../api/tableTitleApi"
import TableTitleTable from "../../components/tables/TableTitleTable"

export default function TableTitlesPage() {
    const navigate = useNavigate()
    const [tableTitles, setTableTitles] = useState<TableTitle[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getTableTitles()
            })
            .then(data => {
                if (data) {
                    const sorted = data.sort((a, b) =>
                        (a.tableTextName || a.tableTextLinc).localeCompare(
                            b.tableTextName || b.tableTextLinc, 'ru'
                        )
                    )
                    setTableTitles(sorted)
                }
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить заголовок таблицы?')) return
        try {
            await deleteTableTitle(id)
            setTableTitles(prev => prev.filter(t => t.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <TableTitleTable
            tableTitles={tableTitles}
            onDelete={handleDelete}
        />
    )
}