// src/pages/Organization/OrganizationPage.tsx
import {useEffect, useState} from 'react'
import {useNavigate, useLocation} from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Checkbox,
    FormControlLabel, Button, Table, TableBody, TableCell,
    TableContainer, TableHead, TableRow, IconButton, Collapse,
    Divider
} from '@mui/material'
import {
    ExpandMore, ExpandLess, Delete, Add, Edit, ArrowBack
} from '@mui/icons-material'
import {getOrganization, updateOrganization} from '../../api/organizationApi'
import type {OrganizationFull} from '../../types/organization'

export default function OrganizationPage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [org, setOrg] = useState<OrganizationFull | null>(null)
    const [originalOrg, setOriginalOrg] = useState<OrganizationFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)
    const [contactsOpen, setContactsOpen] = useState(false)

    const isNew = id === 'new'
    const orgId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            // Пустая организация для создания
            setOrg({
                organizationName: '',
                organizationShortName: '',
                organizationTypeActivity: '',
                oneTerritory: false,
                address: {
                    addressIndex: '',
                    constituentEntity: '',
                    city: '',
                    street: '',
                    house: ''
                },
                signers: [{name: '', position: ''}],
                contacts: []
            })
            setOriginalOrg(null)
            setLoading(false)
            return
        }
        getOrganization(orgId)
            .then(data => {
                const fullData: OrganizationFull = {
                    ...data,
                    address: data.address || {
                        addressIndex: '', constituentEntity: '', city: '', street: '', house: ''
                    },
                    signers: data.signers || [],
                    contacts: data.contacts || []
                }
                setOrg(fullData)
                setOriginalOrg(JSON.parse(JSON.stringify(fullData)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки')
                navigate('/organizations')
            })
    }, [orgId, navigate])

    const handleChange = (field: keyof OrganizationFull, value: any) => {
        if (!org) return
        setOrg({...org, [field]: value})
        setHasChanges(true)
    }

    const handleAddressChange = (field: string, value: string) => {
        if (!org) return
        setOrg({...org, address: {...org.address!, [field]: value}})
        setHasChanges(true)
    }

    const handleContactChange = (index: number, field: string, value: string) => {
        if (!org) return
        const newContacts = [...org.contacts]
        newContacts[index] = {...newContacts[index], [field]: value}
        setOrg({...org, contacts: newContacts})
        setHasChanges(true)
    }

    const addContact = () => {
        if (!org) return
        setOrg({
            ...org,
            contacts: [...org.contacts, {fullName: '', position: '', phones: '', address: ''}]
        })
        setHasChanges(true)
    }

    const removeContact = (index: number) => {
        if (!org) return
        setOrg({
            ...org, contacts: org.contacts.filter((_,
                                                   i) => i !== index)
        })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!org) return
        try {
            if (isNew) {
                // POST для создания
                const res = await fetch('/api/organizations', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(org),
                    credentials: 'include'
                })
                if (!res.ok) {
                    alert('Ошибка создания: ' + res.status)
                    return
                }
                const newOrg = await res.json()
                setOriginalOrg(JSON.parse(JSON.stringify(org)))
                setHasChanges(false)
                navigate(`/organization/${newOrg.id}/edit`)
            } else {
                // PUT для обновления
                await updateOrganization(orgId, org)
                setOriginalOrg(JSON.parse(JSON.stringify(org)))
                setHasChanges(false)
            }
        } catch {
            alert('Ошибка сохранения')
        }
    }

    const handleCancel = () => {
        if (originalOrg) setOrg(JSON.parse(JSON.stringify(originalOrg)))
        setHasChanges(false)
    }

    const handleEdit = () =>
        navigate(`/organization/${orgId}/edit`)
    const handleBack = () =>
        navigate('/organizations')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!org) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{maxWidth: 1000}}>
            <Typography variant="h5" sx={{mb: 2, pb: 1, borderBottom: '2px solid #4caf50'}}>
                {isNew ? 'Новая организация' :
                    isEditMode ? 'Редактирование организации' : 'Просмотр организации'}
            </Typography>

            <Paper sx={{p: 3, mb: 2}}>
                {/* === ОРГАНИЗАЦИЯ === */}
                <Typography variant="h6" sx={{mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1}}>
                    Организация
                </Typography>

                <Stack spacing={2} sx={{pl: 2}}>
                    <TextField
                        label="Полное наименование"
                        value={org.organizationName}
                        onChange={e =>
                            handleChange('organizationName', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={4}
                        disabled={!isEditMode}
                    />

                    <TextField
                        label="Краткое наименование"
                        value={org.organizationShortName}
                        onChange={e =>
                            handleChange('organizationShortName', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />

                    <TextField
                        label="Вид деятельности"
                        value={org.organizationTypeActivity}
                        onChange={e =>
                            handleChange('organizationTypeActivity', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={10}
                        disabled={!isEditMode}
                    />

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={org.oneTerritory}
                                onChange={e =>
                                    handleChange('oneTerritory', e.target.checked)}
                                disabled={!isEditMode}
                            />
                        }
                        label="ОПО на одной территории"
                    />
                </Stack>

                <Divider sx={{my: 3}}/>

                {/* === ПОДПИСАНТЫ === */}
                <Typography variant="h6" sx={{mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1}}>
                    Подписант
                </Typography>

                {isEditMode ? (
                    <>
                        {org.signers.map((signer, index) => (
                            <Box key={index} sx={{display: 'flex', gap: 2, alignItems: 'center', mb: 1}}>
                                <Checkbox
                                    checked={signer.isPrimary || false}
                                    onChange={() => {
                                        const newSigners =
                                            org.signers.map((s,
                                                             i) =>
                                                ({...s, isPrimary: i === index}))
                                        setOrg({...org, signers: newSigners})
                                        setHasChanges(true)
                                    }}
                                />
                                <TextField
                                    label="Должность"
                                    value={signer.position}
                                    onChange={e => {
                                        const newSigners =
                                            [...org.signers]
                                        newSigners[index] = {
                                            ...newSigners[index], position:
                                            e.target.value
                                        }
                                        setOrg({...org, signers: newSigners})
                                        setHasChanges(true)
                                    }}
                                    size="small"
                                    sx={{flex: 1}}
                                />
                                <TextField
                                    label="ФИО"
                                    value={signer.name}
                                    onChange={e => {
                                        const newSigners =
                                            [...org.signers]
                                        newSigners[index] = {
                                            ...newSigners[index], name:
                                            e.target.value
                                        }
                                        setOrg({...org, signers: newSigners})
                                        setHasChanges(true)
                                    }}
                                    size="small"
                                    sx={{flex: 1}}
                                />
                                <IconButton
                                    onClick={() => {
                                        const newSigners =
                                            org.signers.filter((_, i) => i !== index)
                                        setOrg({...org, signers: newSigners})
                                        setHasChanges(true)
                                    }}
                                    color="error"
                                >
                                    <Delete/>
                                </IconButton>
                            </Box>
                        ))}
                        <Button
                            startIcon={<Add/>}
                            onClick={() => {
                                setOrg({
                                    ...org,
                                    signers: [...org.signers, {
                                        name: '', position: '',
                                        isPrimary: false
                                    }]
                                })
                                setHasChanges(true)
                            }}
                            sx={{mt: 1}}
                        >
                            Добавить подписанта
                        </Button>
                    </>
                ) : (
                    // Режим просмотра — только основной, поля рядом
                    <Box sx={{display: 'flex', gap: 2, alignItems: 'center', pl: 2}}>
                        {org.signers.find(s => s.isPrimary) ? (
                            <>
                                <TextField
                                    label="Должность"
                                    value={org.signers.find(s => s.isPrimary)?.position || ''}
                                    fullWidth
                                    disabled
                                />
                                <TextField
                                    label="ФИО"
                                    value={org.signers.find(s => s.isPrimary)?.name || ''}
                                    fullWidth
                                    disabled
                                />
                            </>
                        ) : (
                            <Typography color="text.secondary">Подписант не выбран</Typography>
                        )}
                    </Box>
                )}

                <Divider sx={{my: 3}}/>

                {/* === АДРЕС === */}
                <Typography variant="h6" sx={{mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1}}>
                    Адрес организации
                </Typography>

                <Box sx={{
                    display: 'grid', gridTemplateColumns: '1fr 2fr 1.5fr 1.5fr 80px',
                    gap: 2, pl: 2
                }}>
                    <TextField
                        label="Индекс"
                        type="number"
                        value={org.address?.addressIndex ?? ''}
                        onChange={e => {
                            const value = e.target.value;
                            if (value === '' || /^\d+$/.test(value)) {
                                handleAddressChange('addressIndex', value);
                            }
                        }}
                        slotProps={{
                            htmlInput: {
                                min: 0,
                                step: 1
                            }
                        }}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Субъект РФ"
                        value={org.address?.constituentEntity || ''}
                        onChange={e =>
                            handleAddressChange('constituentEntity', e.target.value)}
                        multiline
                        maxRows={10}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Город"
                        value={org.address?.city || ''}
                        onChange={e =>
                            handleAddressChange('city', e.target.value)}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Улица"
                        value={org.address?.street || ''}
                        onChange={e =>
                            handleAddressChange('street', e.target.value)}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Дом"
                        value={org.address?.house || ''}
                        onChange={e =>
                            handleAddressChange('house', e.target.value)}
                        disabled={!isEditMode}
                    />
                </Box>

                <Divider sx={{my: 3}}/>

                {/* === КОНТАКТЫ (выпадающий) === */}
                <Box sx={{bgcolor: '#f5f5f5', borderRadius: 1, cursor: 'pointer'}}
                     onClick={() => setContactsOpen(!contactsOpen)}>
                    <Box sx={{
                        display: 'flex', justifyContent: 'space-between',
                        alignItems: 'center', p: 1
                    }}>
                        <Typography variant="h6">Контакты организации</Typography>
                        {contactsOpen ? <ExpandLess/> : <ExpandMore/>}
                    </Box>
                </Box>

                <Collapse in={contactsOpen}>
                    <Box sx={{p: 2}}>
                        <TableContainer>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>№</TableCell>
                                        <TableCell>ФИО</TableCell>
                                        <TableCell>Должность</TableCell>
                                        <TableCell>Телефон</TableCell>
                                        <TableCell>Адрес</TableCell>
                                        {isEditMode && <TableCell></TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {org.contacts.map((contact,
                                                       index) => (
                                        <TableRow key={index}>
                                            <TableCell>{index + 1}</TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={contact.fullName}
                                                    onChange={e =>
                                                        handleContactChange(index, 'fullName', e.target.value)}
                                                    size="small"
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={contact.position}
                                                    onChange={e =>
                                                        handleContactChange(index, 'position', e.target.value)}
                                                    size="small"
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={contact.phones}
                                                    onChange={e =>
                                                        handleContactChange(index, 'phones', e.target.value)}
                                                    size="small"
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={contact.address}
                                                    onChange={e =>
                                                        handleContactChange(index, 'address', e.target.value)}
                                                    size="small"
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            {isEditMode && (
                                                <TableCell>
                                                    <IconButton onClick={() => removeContact(index)}
                                                                color="error">
                                                        <Delete/>
                                                    </IconButton>
                                                </TableCell>
                                            )}
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>

                        {isEditMode && (
                            <Button
                                startIcon={<Add/>}
                                onClick={addContact}
                                sx={{mt: 2}}
                            >
                                Добавить контакт
                            </Button>
                        )}
                    </Box>
                </Collapse>
            </Paper>

            {/* Кнопки */}
            <Box sx={{display: 'flex', justifyContent: 'center', gap: 2, mt: 3}}>
                {isNew ? (
                        <>
                            <Button variant="contained" color="success" onClick={handleSave}>
                                Сохранить
                            </Button>
                            <Button variant="outlined" onClick={handleBack}>
                                Назад к списку
                            </Button>
                        </>
                    ) :
                    !isEditMode ? (
                        <>
                            <Button variant="contained" startIcon={<Edit/>} onClick={handleEdit}>
                                Редактировать
                            </Button>
                            <Button variant="outlined" startIcon={<ArrowBack/>} onClick={handleBack}>
                                Назад к списку
                            </Button>
                        </>
                    ) : (
                        hasChanges ? (
                            <>
                                <Button variant="contained" color="success" onClick={handleSave}>
                                    Сохранить
                                </Button>
                                <Button variant="outlined" onClick={handleCancel}>
                                    Отменить
                                </Button>
                            </>
                        ) : (
                            <Button variant="outlined" startIcon={<ArrowBack/>} onClick={handleBack}>
                                Назад к списку
                            </Button>
                        )
                    )}
            </Box>
        </Box>
    )
}