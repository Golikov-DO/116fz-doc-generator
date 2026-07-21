import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button
} from '@mui/material'
import { Edit, ArrowBack } from '@mui/icons-material'
import { getScenario, updateScenario, createScenario } from '../../api/scenarioApi'
import type { ScenarioFull } from '../../types/scenario'

const EMPTY_SCENARIO: ScenarioFull = {
    name: '',
    description: '',
    impactFactor: ''
}

export default function ScenarioPage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [scenario, setScenario] = useState<ScenarioFull | null>(null)
    const [originalScenario, setOriginalScenario] = useState<ScenarioFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    const isNew = id === 'new'
    const scenarioId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            setScenario({ ...EMPTY_SCENARIO })
            setOriginalScenario(null)
            setLoading(false)
            return
        }
        getScenario(scenarioId)
            .then(data => {
                setScenario(data)
                setOriginalScenario(JSON.parse(JSON.stringify(data)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки сценария')
                navigate('/scenarios')
            })
    }, [scenarioId, navigate, isNew])

    const handleChange = (field: keyof ScenarioFull, value: string) => {
        if (!scenario) return
        setScenario({ ...scenario, [field]: value })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!scenario) return
        try {
            if (isNew) {
                const newScenario = await createScenario(scenario)
                setHasChanges(false)
                navigate(`/scenarios/${newScenario.id}/edit`)
            } else {
                await updateScenario(scenarioId, scenario)
                setOriginalScenario(JSON.parse(JSON.stringify(scenario)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalScenario) setScenario(JSON.parse(JSON.stringify(originalScenario)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/scenarios/${scenarioId}/edit`)
    const handleBack = () => navigate('/scenarios')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!scenario) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{ maxWidth: 800 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новый сценарий' :
                    isEditMode ? 'Редактирование сценария' : 'Просмотр сценария'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                <Stack spacing={2}>
                    <TextField
                        label="Наименование"
                        value={scenario.name}
                        onChange={e => handleChange('name', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Описание"
                        value={scenario.description}
                        onChange={e => handleChange('description', e.target.value)}
                        fullWidth
                        multiline
                        rows={4}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Фактор воздействия"
                        value={scenario.impactFactor}
                        onChange={e => handleChange('impactFactor', e.target.value)}
                        fullWidth
                        multiline
                        rows={2}
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