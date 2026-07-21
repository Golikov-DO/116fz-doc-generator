import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button
} from '@mui/material'
import { Edit, ArrowBack } from '@mui/icons-material'
import { getTableTitle, updateTableTitle, createTableTitle } from '../../api/tableTitleApi'
import type { TableTitleFull } from '../../types/tableTitle'

const EMPTY_TABLE_TITLE: TableTitleFull = {
    tableTextLinc: '',
    tableTextName: ''
}

export default function TableTitlePage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [tableTitle, setTableTitle] = useState<TableTitleFull | null>(null)
    const [originalTableTitle, setOriginalTableTitle] = useState<TableTitleFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    const isNew = id === 'new'
    const tableTitleId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            setTableTitle({ ...EMPTY_TABLE_TITLE })
            setOriginalTableTitle(null)
            setLoading(false)
            return
        }
        getTableTitle(tableTitleId)
            .then(data => {
                setTableTitle(data)
                setOriginalTableTitle(JSON.parse(JSON.stringify(data)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки заголовка таблицы')
                navigate('/table-titles')
            })
    }, [tableTitleId, navigate, isNew])

    const handleChange = (field: keyof TableTitleFull, value: string) => {
        if (!tableTitle) return
        setTableTitle({ ...tableTitle, [field]: value })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!tableTitle) return
        try {
            if (isNew) {
                const newTableTitle = await createTableTitle(tableTitle)
                setHasChanges(false)
                navigate(`/table-titles/${newTableTitle.id}/edit`)
            } else {
                await updateTableTitle(tableTitleId, tableTitle)
                setOriginalTableTitle(JSON.parse(JSON.stringify(tableTitle)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalTableTitle) setTableTitle(JSON.parse(JSON.stringify(originalTableTitle)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/table-titles/${tableTitleId}/edit`)
    const handleBack = () => navigate('/table-titles')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!tableTitle) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{ maxWidth: 800 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новый заголовок таблицы' :
                    isEditMode ? 'Редактирование заголовка таблицы' : 'Просмотр заголовка таблицы'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                <Stack spacing={2}>
                    <TextField
                        label="Текст ссылки"
                        value={tableTitle.tableTextLinc}
                        onChange={e => handleChange('tableTextLinc', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Название таблицы"
                        value={tableTitle.tableTextName}
                        onChange={e => handleChange('tableTextName', e.target.value)}
                        fullWidth
                        multiline
                        rows={4}
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