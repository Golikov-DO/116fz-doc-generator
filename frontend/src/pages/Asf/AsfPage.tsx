// src/pages/Asf/AsfPage.tsx
import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    IconButton, Divider, Checkbox
} from '@mui/material'
import {
    Delete, Add, Edit, ArrowBack
} from '@mui/icons-material'
import { getAsf, updateAsf, createAsf } from '../../api/asfApi'
import type { AsfFull } from '../../types/asf'
import AccordionSection from '../../components/AccordionSection'
import AsfImagesSection from '../../components/AsfImagesSection'

const EMPTY_CERTIFICATE = {
    certNumber: '',
    certSeries: '',
    issuedBy: '',
    issueBasis: '',
    issueDate: null as string | null,
    validUntil: null as string | null
}

const EMPTY_PERSONNEL = {
    staffByStaffing: 0,
    staffByList: 0,
    certifiedTotal: 0,
    qualifiedTotal: 0,
    firstClass: 0,
    secondClass: 0,
    thirdClass: 0,
    internationalClass: 0
}

const EMPTY_SPECIALISTS = {
    totalCount: 0,
    asrTp: 0,
    asrLrnTer: 0,
    gzsr: 0,
    psr: 0,
    driver: 0,
    asrLrnSea: 0
}

const EMPTY_DEPLOYMENT = {
    responsibilityArea: '',
    deploymentPlace: '',
    dutyOfficerTelephone: '',
    contactTelephone: '',
    eMail: '',
    numberBuildings: '',
    totalArea: ''
}

const EMPTY_ASF: AsfFull = {
    fullName: '',
    fullNameGen: '',
    shortName: '',
    statusShort: '',
    certificate: { ...EMPTY_CERTIFICATE },
    personnel: { ...EMPTY_PERSONNEL },
    specialists: { ...EMPTY_SPECIALISTS },
    deployment: { ...EMPTY_DEPLOYMENT },
    signers: [],
    workTypes: [],
    images: []
}

// === Компонент секции-аккордеона ===
export default function AsfPage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [asf, setAsf] = useState<AsfFull | null>(null)
    const [originalAsf, setOriginalAsf] = useState<AsfFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    // Состояния раскрытия секций
    const [certOpen, setCertOpen] = useState(false)
    const [deployOpen, setDeployOpen] = useState(false)
    const [personnelOpen, setPersonnelOpen] = useState(false)
    const [specialistsOpen, setSpecialistsOpen] = useState(false)
    const [signersOpen, setSignersOpen] = useState(false)
    const [workTypesOpen, setWorkTypesOpen] = useState(false)
    const [imagesOpen, setImagesOpen] = useState(false)

    const isNew = id === 'new'
    const asfId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            setAsf({ ...EMPTY_ASF })
            setOriginalAsf(null)
            setLoading(false)
            return
        }
        getAsf(asfId)
            .then(data => {
                const fullData: AsfFull = {
                    ...EMPTY_ASF,
                    ...data,
                    certificate: data.certificate ?? { ...EMPTY_CERTIFICATE },
                    personnel: data.personnel ?? { ...EMPTY_PERSONNEL },
                    specialists: data.specialists ?? { ...EMPTY_SPECIALISTS },
                    deployment: data.deployment ?? { ...EMPTY_DEPLOYMENT },
                    signers: data.signers ?? [],
                    workTypes: data.workTypes ?? [],
                    images: data.images ?? []
                }
                setAsf(fullData)
                setOriginalAsf(JSON.parse(JSON.stringify(fullData)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки АСФ')
                navigate('/asfs')
            })
    }, [asfId, navigate, isNew])

    const handleChange = (field: keyof AsfFull, value: any) => {
        if (!asf) return
        setAsf({ ...asf, [field]: value })
        setHasChanges(true)
    }

    const handleCertificateChange = (field: string, value: any) => {
        if (!asf) return
        setAsf({ ...asf, certificate: { ...(asf.certificate || EMPTY_CERTIFICATE), [field]: value } })
        setHasChanges(true)
    }

    const handlePersonnelChange = (field: string, value: number) => {
        if (!asf || value < 0) return
        setAsf({ ...asf, personnel: { ...(asf.personnel || EMPTY_PERSONNEL), [field]: value } })
        setHasChanges(true)
    }

    const handleSpecialistsChange = (field: string, value: number) => {
        if (!asf || value < 0) return
        setAsf({ ...asf, specialists: { ...(asf.specialists || EMPTY_SPECIALISTS), [field]: value } })
        setHasChanges(true)
    }

    const handleDeploymentChange = (field: string, value: string) => {
        if (!asf) return
        setAsf({ ...asf, deployment: { ...(asf.deployment || EMPTY_DEPLOYMENT), [field]: value } })
        setHasChanges(true)
    }

    // === Подписанты ===
    const handleSignerChange = (index: number, field: string, value: any) => {
        if (!asf) return
        const newSigners = [...asf.signers]
        newSigners[index] = { ...newSigners[index], [field]: value }
        setAsf({ ...asf, signers: newSigners })
        setHasChanges(true)
    }

    const addSigner = () => {
        if (!asf) return
        setAsf({
            ...asf,
            signers: [...asf.signers, { name: '', position: '', isPrimary: false }]
        })
        setHasChanges(true)
    }

    const removeSigner = (index: number) => {
        if (!asf) return
        setAsf({ ...asf, signers: asf.signers.filter((_, i) => i !== index) })
        setHasChanges(true)
    }

    // === Типы работ ===
    const handleWorkTypeChange = (index: number, value: string) => {
        if (!asf) return
        const newWorkTypes = [...asf.workTypes]
        newWorkTypes[index] = { ...newWorkTypes[index], name: value }
        setAsf({ ...asf, workTypes: newWorkTypes })
        setHasChanges(true)
    }

    const addWorkType = () => {
        if (!asf) return
        setAsf({
            ...asf,
            workTypes: [...asf.workTypes, { name: '' }]
        })
        setHasChanges(true)
    }

    const removeWorkType = (index: number) => {
        if (!asf) return
        setAsf({ ...asf, workTypes: asf.workTypes.filter((_, i) => i !== index) })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!asf) return
        try {
            if (isNew) {
                const newAsf = await createAsf(asf)
                setHasChanges(false)
                navigate(`/asf/${newAsf.id}/edit`)
            } else {
                await updateAsf(asfId, asf)
                setOriginalAsf(JSON.parse(JSON.stringify(asf)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalAsf) setAsf(JSON.parse(JSON.stringify(originalAsf)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/asf/${asfId}/edit`)
    const handleBack = () => navigate('/asfs')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!asf) return <Typography>Не найдено</Typography>

    const cert = asf.certificate || EMPTY_CERTIFICATE
    const pers = asf.personnel || EMPTY_PERSONNEL
    const spec = asf.specialists || EMPTY_SPECIALISTS
    const deploy = asf.deployment || EMPTY_DEPLOYMENT

    return (
        <Box sx={{ maxWidth: 1200 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новая АСФ' :
                    isEditMode ? 'Редактирование АСФ' : 'Просмотр АСФ'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                {/* === ОСНОВНАЯ ИНФОРМАЦИЯ === */}
                <Typography variant="h6" sx={{ mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1 }}>
                    Основная информация
                </Typography>

                <Stack spacing={2} sx={{ pl: 2 }}>
                    <TextField
                        label="Полное наименование"
                        value={asf.fullName}
                        onChange={e => handleChange('fullName', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={4}
                        disabled={!isEditMode}
                    />

                    <TextField
                        label="Полное наименование (род. падеж)"
                        value={asf.fullNameGen}
                        onChange={e => handleChange('fullNameGen', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={4}
                        disabled={!isEditMode}
                    />

                    <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
                        <TextField
                            label="Краткое наименование"
                            value={asf.shortName}
                            onChange={e => handleChange('shortName', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Статус"
                            value={asf.statusShort}
                            onChange={e => handleChange('statusShort', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                    </Box>
                </Stack>

                <Divider sx={{ my: 3 }} />

                {/* === СВИДЕТЕЛЬСТВО АСФ === */}
                <AccordionSection
                    title="Свидетельство АСФ"
                    open={certOpen}
                    setOpen={setCertOpen}
                    isEditMode={isEditMode}
                >
                    <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 2 }}>
                        <TextField
                            label="Номер свидетельства"
                            value={cert.certNumber}
                            onChange={e => handleCertificateChange('certNumber', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Серия"
                            value={cert.certSeries}
                            onChange={e => handleCertificateChange('certSeries', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Основание выдачи"
                            value={cert.issueBasis}
                            onChange={e => handleCertificateChange('issueBasis', e.target.value)}
                            disabled={!isEditMode}
                        />
                    </Box>
                    <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 2, mt: 2 }}>
                        <TextField
                            label="Кем выдано"
                            value={cert.issuedBy}
                            onChange={e => handleCertificateChange('issuedBy', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Дата выдачи"
                            type="date"
                            value={cert.issueDate || ''}
                            onChange={e => handleCertificateChange('issueDate', e.target.value || null)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Действителен до"
                            type="date"
                            value={cert.validUntil || ''}
                            onChange={e => handleCertificateChange('validUntil', e.target.value || null)}
                            disabled={!isEditMode}
                        />
                    </Box>
                </AccordionSection>

                {/* === СОСТАВ И РАЗМЕЩЕНИЕ === */}
                <AccordionSection
                    title="Состав и размещение"
                    open={deployOpen}
                    setOpen={setDeployOpen}
                    isEditMode={isEditMode}
                >
                    <TextField
                        label="Зона ответственности"
                        value={deploy.responsibilityArea}
                        onChange={e => handleDeploymentChange('responsibilityArea', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={6}
                        disabled={!isEditMode}
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        label="Место размещения"
                        value={deploy.deploymentPlace}
                        onChange={e => handleDeploymentChange('deploymentPlace', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={4}
                        disabled={!isEditMode}
                        sx={{ mb: 2 }}
                    />
                    <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 2 }}>
                        <TextField
                            label="Телефон дежурного"
                            value={deploy.dutyOfficerTelephone}
                            onChange={e => handleDeploymentChange('dutyOfficerTelephone', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Контактный телефон"
                            value={deploy.contactTelephone}
                            onChange={e => handleDeploymentChange('contactTelephone', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="E-mail"
                            value={deploy.eMail}
                            onChange={e => handleDeploymentChange('eMail', e.target.value)}
                            disabled={!isEditMode}
                        />
                    </Box>
                    <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2, mt: 2 }}>
                        <TextField
                            label="Количество зданий"
                            value={deploy.numberBuildings}
                            onChange={e => handleDeploymentChange('numberBuildings', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Общая площадь"
                            value={deploy.totalArea}
                            onChange={e => handleDeploymentChange('totalArea', e.target.value)}
                            disabled={!isEditMode}
                        />
                    </Box>
                </AccordionSection>

                {/* === КАДРОВЫЙ СОСТАВ === */}
                <AccordionSection
                    title="Кадровый состав"
                    open={personnelOpen}
                    setOpen={setPersonnelOpen}
                    isEditMode={isEditMode}
                >
                    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 2 }}>
                        <TextField
                            label="Штат по штатному расписанию"
                            type="number"
                            value={pers.staffByStaffing}
                            onChange={e => handlePersonnelChange('staffByStaffing', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Штат по списку"
                            type="number"
                            value={pers.staffByList}
                            onChange={e => handlePersonnelChange('staffByList', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Всего аттестовано"
                            type="number"
                            value={pers.certifiedTotal}
                            onChange={e => handlePersonnelChange('certifiedTotal', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Всего квалифицировано"
                            type="number"
                            value={pers.qualifiedTotal}
                            onChange={e => handlePersonnelChange('qualifiedTotal', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                    </Box>
                    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 2, mt: 2 }}>
                        <TextField
                            label="I класс"
                            type="number"
                            value={pers.firstClass}
                            onChange={e => handlePersonnelChange('firstClass', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="II класс"
                            type="number"
                            value={pers.secondClass}
                            onChange={e => handlePersonnelChange('secondClass', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="III класс"
                            type="number"
                            value={pers.thirdClass}
                            onChange={e => handlePersonnelChange('thirdClass', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Международный класс"
                            type="number"
                            value={pers.internationalClass}
                            onChange={e => handlePersonnelChange('internationalClass', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                    </Box>
                </AccordionSection>

                {/* === СПЕЦИАЛИСТЫ ПО НАПРАВЛЕНИЯМ === */}
                <AccordionSection
                    title="Специалисты по направлениям"
                    open={specialistsOpen}
                    setOpen={setSpecialistsOpen}
                    isEditMode={isEditMode}
                >
                    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 2 }}>
                        <TextField
                            label="Всего специалистов"
                            type="number"
                            value={spec.totalCount}
                            onChange={e => handleSpecialistsChange('totalCount', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="АСР ТП"
                            type="number"
                            value={spec.asrTp}
                            onChange={e => handleSpecialistsChange('asrTp', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="АСР ЛРН Тер"
                            type="number"
                            value={spec.asrLrnTer}
                            onChange={e => handleSpecialistsChange('asrLrnTer', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="ГЗСР"
                            type="number"
                            value={spec.gzsr}
                            onChange={e => handleSpecialistsChange('gzsr', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                    </Box>
                    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 2, mt: 2 }}>
                        <TextField
                            label="ПСР"
                            type="number"
                            value={spec.psr}
                            onChange={e => handleSpecialistsChange('psr', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Водитель"
                            type="number"
                            value={spec.driver}
                            onChange={e => handleSpecialistsChange('driver', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="АСР ЛРН Мор"
                            type="number"
                            value={spec.asrLrnSea}
                            onChange={e => handleSpecialistsChange('asrLrnSea', Number(e.target.value))}
                            disabled={!isEditMode}
                        />
                    </Box>
                </AccordionSection>

                {/* === ПОДПИСАНТЫ ОТ АСФ === */}
                <AccordionSection
                    title="Подписанты от АСФ"
                    open={signersOpen}
                    setOpen={setSignersOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add />} onClick={addSigner}>
                            Добавить подписанта
                        </Button>
                    }
                >
                    {asf.signers.length > 0 ? (
                        <TableContainer>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>№</TableCell>
                                        <TableCell>ФИО</TableCell>
                                        <TableCell>Должность</TableCell>
                                        <TableCell>Основной</TableCell>
                                        {isEditMode && <TableCell></TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {asf.signers.map((signer, index) => (
                                        <TableRow key={signer.id || index}>
                                            <TableCell>{index + 1}</TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={signer.name}
                                                    onChange={e => handleSignerChange(index, 'name', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={signer.position}
                                                    onChange={e => handleSignerChange(index, 'position', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <Checkbox
                                                    checked={signer.isPrimary || false}
                                                    onChange={() => {
                                                        const newSigners = asf.signers.map((s, i) => ({
                                                            ...s,
                                                            isPrimary: i === index
                                                        }))
                                                        setAsf({ ...asf, signers: newSigners })
                                                        setHasChanges(true)
                                                    }}
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            {isEditMode && (
                                                <TableCell>
                                                    <IconButton onClick={() => removeSigner(index)} color="error" size="small">
                                                        <Delete />
                                                    </IconButton>
                                                </TableCell>
                                            )}
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography color="text.secondary">Подписанты не добавлены</Typography>
                    )}
                </AccordionSection>

                {/* === ТИП ВЫПОЛНЯЕМЫХ РАБОТ === */}
                <AccordionSection
                    title="Тип выполняемых работ"
                    open={workTypesOpen}
                    setOpen={setWorkTypesOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add />} onClick={addWorkType}>
                            Добавить тип работ
                        </Button>
                    }
                >
                    {asf.workTypes.length > 0 ? (
                        <TableContainer>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>№</TableCell>
                                        <TableCell>Наименование</TableCell>
                                        {isEditMode && <TableCell></TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {asf.workTypes.map((wt, index) => (
                                        <TableRow key={wt.id || index}>
                                            <TableCell>{index + 1}</TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={wt.name}
                                                    onChange={e => handleWorkTypeChange(index, e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            {isEditMode && (
                                                <TableCell>
                                                    <IconButton onClick={() => removeWorkType(index)} color="error" size="small">
                                                        <Delete />
                                                    </IconButton>
                                                </TableCell>
                                            )}
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography color="text.secondary">Типы работ не добавлены</Typography>
                    )}
                </AccordionSection>

                {/* === ПРИЛОЖЕНИЯ === */}
                {!isNew ? (
                    <AccordionSection
                        title="Приложения"
                        open={imagesOpen}
                        setOpen={setImagesOpen}
                        isEditMode={isEditMode}
                    >
                        <AsfImagesSection
                            asfId={asfId}
                            images={asf.images.map(img => ({ ...img, id: img.id ?? 0 }))}
                            isEditMode={isEditMode}
                            onImagesChange={(newImages) => {
                                setAsf({ ...asf, images: newImages })
                                setHasChanges(true)
                            }}
                        />
                    </AccordionSection>
                ) : (
                    <Paper sx={{ p: 2, mt: 2 }}>
                <Typography color="warning.main">
                    Приложения (сканы документов) можно добавить только после сохранения новой АСФ.
                </Typography>
            </Paper>
            )}
            </Paper>

            {/* Кнопки */}
            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 3 }}>
                {isNew ? (
                    <>
                        <Button variant="contained" color="success" onClick={handleSave}>
                            Сохранить
                        </Button>
                        <Button variant="outlined" onClick={handleBack}>
                            Назад к списку
                        </Button>
                    </>
                ) : !isEditMode ? (
                    <>
                        <Button variant="contained" startIcon={<Edit />} onClick={handleEdit}>
                            Редактировать
                        </Button>
                        <Button variant="outlined" startIcon={<ArrowBack />} onClick={handleBack}>
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
                        <Button variant="outlined" startIcon={<ArrowBack />} onClick={handleBack}>
                            Назад к списку
                        </Button>
                    )
                )}
            </Box>
        </Box>
    )
}