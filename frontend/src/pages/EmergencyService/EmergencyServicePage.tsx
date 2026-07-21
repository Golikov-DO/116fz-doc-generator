import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button
} from '@mui/material'
import { Edit, ArrowBack } from '@mui/icons-material'
import { getEmergencyService, updateEmergencyService, createEmergencyService } from '../../api/emergencyServiceApi'
import type { EmergencyServiceFull } from '../../types/emergencyService'

const EMPTY_SERVICE: EmergencyServiceFull = {
    serviceName: '',
    positionContact: '',
    phone: '',
    address: ''
}

export default function EmergencyServicePage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [service, setService] = useState<EmergencyServiceFull | null>(null)
    const [originalService, setOriginalService] = useState<EmergencyServiceFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    const isNew = id === 'new'
    const serviceId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            setService({ ...EMPTY_SERVICE })
            setOriginalService(null)
            setLoading(false)
            return
        }
        getEmergencyService(serviceId)
            .then(data => {
                setService(data)
                setOriginalService(JSON.parse(JSON.stringify(data)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки экстренной службы')
                navigate('/emergency-services')
            })
    }, [serviceId, navigate, isNew])

    const handleChange = (field: keyof EmergencyServiceFull, value: string) => {
        if (!service) return
        setService({ ...service, [field]: value })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!service) return
        try {
            if (isNew) {
                const newService = await createEmergencyService(service)
                setHasChanges(false)
                navigate(`/emergency-services/${newService.id}/edit`)
            } else {
                await updateEmergencyService(serviceId, service)
                setOriginalService(JSON.parse(JSON.stringify(service)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalService) setService(JSON.parse(JSON.stringify(originalService)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/emergency-services/${serviceId}/edit`)
    const handleBack = () => navigate('/emergency-services')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!service) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{ maxWidth: 800 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новая экстренная служба' :
                    isEditMode ? 'Редактирование экстренной службы' : 'Просмотр экстренной службы'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                <Stack spacing={2}>
                    <TextField
                        label="Наименование службы"
                        value={service.serviceName}
                        onChange={e => handleChange('serviceName', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Должность / Контактное лицо"
                        value={service.positionContact}
                        onChange={e => handleChange('positionContact', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Телефон"
                        value={service.phone}
                        onChange={e => handleChange('phone', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Адрес"
                        value={service.address}
                        onChange={e => handleChange('address', e.target.value)}
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