import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Divider, Alert
} from '@mui/material'
import { Edit, ArrowBack } from '@mui/icons-material'
import { getCity, updateCity, createCity } from '../../api/cityApi'
import type { CityFull, RegionalAuthority } from '../../types/city'
import AccordionSection from '../../components/AccordionSection'

// 4 фиксированных органа с шаблонными названиями (регион дописывается пользователем)
const DEFAULT_AUTHORITIES: RegionalAuthority[] = [
    { id: 0, name: 'Главное управление МЧС по ______', department: '', phoneNumber: '', address: '' },
    { id: 0, name: '______ управление Ростехнадзора', department: '', phoneNumber: '', address: '' },
    { id: 0, name: 'Структурное подразделение ______', department: '', phoneNumber: '', address: '' },
    { id: 0, name: '______ межрегиональное управление Росприроднадзора по ______', department: '', phoneNumber: '', address: '' },
]

const EMPTY_CITY: CityFull = {
    id: 0,
    geoRelief: '',
    geoGeology: '',
    climatDesc: '',
    hydroDesc: '',
    infraTransport: '',
    infraEngineering: '',
    infraOrganizations: '',
    nearbyTowns: '',
    massPeoplePlaces: '',
    adminStatus: '',
    distCenters: '',
    cityName: '',
    regionalAuthorities: []
}

export default function CityPage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [city, setCity] = useState<CityFull | null>(null)
    const [originalCity, setOriginalCity] = useState<CityFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    const [mainOpen, setMainOpen] = useState(true)
    const [geoOpen, setGeoOpen] = useState(false)
    const [infraOpen, setInfraOpen] = useState(false)
    const [authOpen, setAuthOpen] = useState(true)

    const isNew = id === 'new'
    const cityId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            setCity({
                ...EMPTY_CITY,
                regionalAuthorities: DEFAULT_AUTHORITIES.map(a => ({ ...a }))
            })
            setOriginalCity(null)
            setLoading(false)
            return
        }
        getCity(cityId)
            .then(data => {
                const fullData: CityFull = {
                    ...EMPTY_CITY,
                    ...data,
                    regionalAuthorities: data.regionalAuthorities ?? []
                }
                setCity(fullData)
                setOriginalCity(JSON.parse(JSON.stringify(fullData)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки региона')
                navigate('/cities')
            })
    }, [cityId, navigate, isNew])

    const handleChange = (field: keyof CityFull, value: any) => {
        if (!city) return
        setCity({ ...city, [field]: value })
        setHasChanges(true)
    }

    const handleAuthorityChange = (index: number, field: keyof RegionalAuthority, value: string) => {
        if (!city) return
        const newAuths = [...city.regionalAuthorities]
        newAuths[index] = { ...newAuths[index], [field]: value }
        setCity({ ...city, regionalAuthorities: newAuths })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!city) return
        try {
            if (isNew) {
                const newCity = await createCity(city)
                setHasChanges(false)
                navigate(`/cities/${newCity.id}/edit`)
            } else {
                await updateCity(cityId, city)
                setOriginalCity(JSON.parse(JSON.stringify(city)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalCity) setCity(JSON.parse(JSON.stringify(originalCity)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/cities/${cityId}/edit`)
    const handleBack = () => navigate('/cities')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!city) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{ maxWidth: 1200 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новый регион' :
                    isEditMode ? 'Редактирование региона' : 'Просмотр региона'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                <AccordionSection
                    title="Основная информация"
                    open={mainOpen}
                    setOpen={setMainOpen}
                    isEditMode={isEditMode}
                >
                    <Stack spacing={2} sx={{ pl: 2 }}>
                        <TextField
                            label="Наименование региона"
                            value={city.cityName}
                            onChange={e => handleChange('cityName', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Административный статус"
                            value={city.adminStatus}
                            onChange={e => handleChange('adminStatus', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Расстояние до центров"
                            value={city.distCenters}
                            onChange={e => handleChange('distCenters', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                    </Stack>
                </AccordionSection>

                <Divider sx={{ my: 3 }} />

                <AccordionSection
                    title="Геология и климат"
                    open={geoOpen}
                    setOpen={setGeoOpen}
                    isEditMode={isEditMode}
                >
                    <Stack spacing={2} sx={{ pl: 2 }}>
                        <TextField label="Рельеф" value={city.geoRelief} onChange={e => handleChange('geoRelief', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Геология" value={city.geoGeology} onChange={e => handleChange('geoGeology', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Климат" value={city.climatDesc} onChange={e => handleChange('climatDesc', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Гидрография" value={city.hydroDesc} onChange={e => handleChange('hydroDesc', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                    </Stack>
                </AccordionSection>

                <Divider sx={{ my: 3 }} />

                <AccordionSection
                    title="Инфраструктура"
                    open={infraOpen}
                    setOpen={setInfraOpen}
                    isEditMode={isEditMode}
                >
                    <Stack spacing={2} sx={{ pl: 2 }}>
                        <TextField label="Транспорт" value={city.infraTransport} onChange={e => handleChange('infraTransport', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Инженерные сети" value={city.infraEngineering} onChange={e => handleChange('infraEngineering', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Организации" value={city.infraOrganizations} onChange={e => handleChange('infraOrganizations', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Близлежащие населённые пункты" value={city.nearbyTowns} onChange={e => handleChange('nearbyTowns', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                        <TextField label="Места массового пребывания людей" value={city.massPeoplePlaces} onChange={e => handleChange('massPeoplePlaces', e.target.value)} fullWidth multiline disabled={!isEditMode} />
                    </Stack>
                </AccordionSection>

                <Divider sx={{ my: 3 }} />

                <AccordionSection
                    title="Органы региональной власти"
                    open={authOpen}
                    setOpen={setAuthOpen}
                    isEditMode={isEditMode}
                >
                    {isEditMode && (
                        <Alert severity="info" sx={{ mb: 2 }}>
                            Впишите недостающие данные (название региона) в поля ниже
                        </Alert>
                    )}
                    <TableContainer>
                        <Table size="small" sx={{ '& .MuiTableCell-root': { verticalAlign: 'top', whiteSpace: 'normal' } }}>
                            <TableHead>
                                <TableRow>
                                    <TableCell style={{ width: 40 }}>№</TableCell>
                                    <TableCell style={{ width: '30%' }}>Наименование</TableCell>
                                    <TableCell style={{ width: '20%' }}>Департамент</TableCell>
                                    <TableCell style={{ width: '20%' }}>Телефон</TableCell>
                                    <TableCell style={{ width: '25%' }}>Адрес</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {city.regionalAuthorities.map((auth, index) => (
                                    <TableRow key={index}>
                                        <TableCell>{index + 1}</TableCell>
                                        <TableCell style={{ width: '30%' }}>
                                            <TextField
                                                value={auth.name}
                                                onChange={e => handleAuthorityChange(index, 'name', e.target.value)}
                                                size="small"
                                                multiline
                                                minRows={1}
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell style={{ width: '20%' }}>
                                            <TextField
                                                value={auth.department}
                                                onChange={e => handleAuthorityChange(index, 'department', e.target.value)}
                                                size="small"
                                                multiline
                                                minRows={1}
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell style={{ width: '20%' }}>
                                            <TextField
                                                value={auth.phoneNumber}
                                                onChange={e => handleAuthorityChange(index, 'phoneNumber', e.target.value)}
                                                size="small"
                                                multiline
                                                minRows={1}
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell style={{ width: '25%' }}>
                                            <TextField
                                                value={auth.address}
                                                onChange={e => handleAuthorityChange(index, 'address', e.target.value)}
                                                size="small"
                                                multiline
                                                minRows={1}
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </AccordionSection>
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