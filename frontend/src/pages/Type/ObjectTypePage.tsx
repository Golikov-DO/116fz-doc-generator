import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button
} from '@mui/material'
import { Edit, ArrowBack } from '@mui/icons-material'
import { getObjectType, updateObjectType, createObjectType } from '../../api/objectTypeApi'
import type { ReferenceTypeFull } from '../../types/objectType'

const EMPTY_TYPE: ReferenceTypeFull = {
    type: '',
    typeDefinition: ''
}

export default function ObjectTypePage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [objectType, setObjectType] = useState<ReferenceTypeFull | null>(null)
    const [originalType, setOriginalType] = useState<ReferenceTypeFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    const isNew = id === 'new'
    const typeId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            setObjectType({ ...EMPTY_TYPE })
            setOriginalType(null)
            setLoading(false)
            return
        }
        getObjectType(typeId)
            .then(data => {
                setObjectType(data)
                setOriginalType(JSON.parse(JSON.stringify(data)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки типа объекта')
                navigate('/types')
            })
    }, [typeId, navigate, isNew])

    const handleChange = (field: keyof ReferenceTypeFull, value: string) => {
        if (!objectType) return
        setObjectType({ ...objectType, [field]: value })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!objectType) return
        try {
            if (isNew) {
                const newType = await createObjectType(objectType)
                setHasChanges(false)
                navigate(`/types/${newType.id}/edit`)
            } else {
                await updateObjectType(typeId, objectType)
                setOriginalType(JSON.parse(JSON.stringify(objectType)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalType) setObjectType(JSON.parse(JSON.stringify(originalType)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/types/${typeId}/edit`)
    const handleBack = () => navigate('/types')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!objectType) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{ maxWidth: 800 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новый тип объекта' :
                    isEditMode ? 'Редактирование типа объекта' : 'Просмотр типа объекта'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                <Stack spacing={2}>
                    <TextField
                        label="Тип объекта"
                        value={objectType.type}
                        onChange={e => handleChange('type', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Определение типа"
                        value={objectType.typeDefinition}
                        onChange={e => handleChange('typeDefinition', e.target.value)}
                        fullWidth
                        multiline
                        rows={8}
                        disabled={!isEditMode}
                    />
                </Stack>
            </Paper>

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 3 }}>
                {isNew ? (
                    <>
                        <Button variant="contained" color="success" onClick={handleSave}>Сохранить</Button>
                        <Button variant="outlined" onClick={handleBack}>Назад к списку</Button>
                    </>
                ) : !isEditMode ? (
                    <>
                        <Button variant="contained" startIcon={<Edit />} onClick={handleEdit}>Редактировать</Button>
                        <Button variant="outlined" startIcon={<ArrowBack />} onClick={handleBack}>Назад к списку</Button>
                    </>
                ) : (
                    hasChanges ? (
                        <>
                            <Button variant="contained" color="success" onClick={handleSave}>Сохранить</Button>
                            <Button variant="outlined" onClick={handleCancel}>Отменить</Button>
                        </>
                    ) : (
                        <Button variant="outlined" startIcon={<ArrowBack />} onClick={handleBack}>Назад к списку</Button>
                    )
                )}
            </Box>
        </Box>
    )
}