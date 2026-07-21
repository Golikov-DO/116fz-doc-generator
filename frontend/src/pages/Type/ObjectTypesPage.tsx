import {useEffect, useState} from "react"
import {useNavigate} from "react-router-dom"
import {getObjectTypes, deleteObjectType, type ReferenceType} from "../../api/objectTypeApi"
import ObjectTypeTable from "../../components/tables/ObjectTypeTable"

export default function ObjectTypesPage() {
    const navigate = useNavigate()
    const [objectTypes, setObjectTypes] = useState<ReferenceType[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetch('/api/auth/me', {credentials: 'include'})
            .then(res => {
                if (!res.ok) {
                    navigate('/login')
                    return
                }
                return getObjectTypes()
            })
            .then(data => {
                if (data) {
                    const sorted = data.sort((a, b) =>
                        a.type.localeCompare(b.type, 'ru')
                    )
                    if (data) setObjectTypes(sorted)
                }
            })
            .catch(() => navigate('/login'))
            .finally(() => setLoading(false))
    }, [navigate])

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить тип объекта?')) return
        try {
            await deleteObjectType(id)
            setObjectTypes(objectTypes.filter(t => t.id !== id))
        } catch {
            alert('Ошибка удаления')
        }
    }

    if (loading) return <div>Загрузка...</div>

    return (
        <ObjectTypeTable
            objectTypes={objectTypes}
            onDelete={handleDelete}
        />
    )
}