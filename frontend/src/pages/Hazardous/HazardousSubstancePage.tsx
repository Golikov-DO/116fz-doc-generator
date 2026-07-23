import { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box, Button,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    IconButton, Divider
} from '@mui/material'
import { Delete, Edit, ArrowBack } from '@mui/icons-material'
import { getHazardousSubstance, updateHazardousSubstance, createHazardousSubstance, getHazardousParams } from '../../api/hazardousSubstanceApi'
import type { HazardousSubstanceFull, HazardousParamValue } from '../../types/hazardousSubstance'
import AccordionSection from '../../components/AccordionSection'

const EMPTY_SUBSTANCE: HazardousSubstanceFull = {
    id: 0,
    name: '',
    nameShort: '',
    values: []
}

function sortBySectionNo(a: HazardousParamValue, b: HazardousParamValue): number {
    const partsA = a.sectionNo.split('.').map(Number)
    const partsB = b.sectionNo.split('.').map(Number)
    const len = Math.max(partsA.length, partsB.length)
    for (let i = 0; i < len; i++) {
        const numA = partsA[i] ?? 0
        const numB = partsB[i] ?? 0
        if (numA !== numB) return numA - numB
    }
    return 0
}

export default function HazardousSubstancePage() {
    const navigate = useNavigate()
    const location = useLocation()
    const pathParts = location.pathname.split('/').filter(Boolean)
    const id = pathParts[1]

    const [substance, setSubstance] = useState<HazardousSubstanceFull | null>(null)
    const [originalSubstance, setOriginalSubstance] = useState<HazardousSubstanceFull | null>(null)
    const [hasChanges, setHasChanges] = useState(false)
    const [loading, setLoading] = useState(true)

    const [mainOpen, setMainOpen] = useState(true)
    const [paramsOpen, setParamsOpen] = useState(true)

    const isNew = id === 'new'
    const substanceId = isNew ? 0 : Number(id)

    const isEditMode = location.pathname.endsWith('/edit') || isNew

    useEffect(() => {
        if (isNew) {
            getHazardousParams()
                .then(params => {
                    const sorted = params.sort((a, b) => {
                        const partsA = a.sectionNo.split('.').map(Number)
                        const partsB = b.sectionNo.split('.').map(Number)
                        const len = Math.max(partsA.length, partsB.length)
                        for (let i = 0; i < len; i++) {
                            const na = partsA[i] ?? 0
                            const nb = partsB[i] ?? 0
                            if (na !== nb) return na - nb
                        }
                        return 0
                    })
                    setSubstance({
                        ...EMPTY_SUBSTANCE,
                        values: sorted.map(p => ({
                            paramId: p.id,
                            sectionNo: p.sectionNo,
                            title: p.title,
                            valueText: '',
                            sourceInfo: ''
                        }))
                    })
                    setOriginalSubstance(null)
                    setLoading(false)
                })
                .catch(() => {
                    alert('Ошибка загрузки параметров')
                    navigate('/hazardous-substances')
                })
            return
        }
        getHazardousSubstance(substanceId)
            .then(data => {
                const fullData: HazardousSubstanceFull = {
                    ...EMPTY_SUBSTANCE,
                    ...data,
                    values: (data.values ?? []).sort(sortBySectionNo)
                }
                setSubstance(fullData)
                setOriginalSubstance(JSON.parse(JSON.stringify(fullData)))
                setLoading(false)
            })
            .catch(() => {
                alert('Ошибка загрузки опасного вещества')
                navigate('/hazardous-substances')
            })
    }, [substanceId, navigate, isNew])

    const handleChange = (field: keyof HazardousSubstanceFull, value: any) => {
        if (!substance) return
        setSubstance({ ...substance, [field]: value })
        setHasChanges(true)
    }

    const handleParamChange = (index: number, field: keyof HazardousParamValue, value: string) => {
        if (!substance) return
        const newValues = [...substance.values]
        newValues[index] = { ...newValues[index], [field]: value }
        setSubstance({ ...substance, values: newValues })
        setHasChanges(true)
    }

    const removeParam = (index: number) => {
        if (!substance) return
        setSubstance({ ...substance, values: substance.values.filter((_, i) => i !== index) })
        setHasChanges(true)
    }

    const handleSave = async () => {
        if (!substance) return
        try {
            if (isNew) {
                const newSubstance = await createHazardousSubstance(substance)
                setHasChanges(false)
                navigate(`/hazardous-substances/${newSubstance.id}/edit`)
            } else {
                await updateHazardousSubstance(substanceId, substance)
                setOriginalSubstance(JSON.parse(JSON.stringify(substance)))
                setHasChanges(false)
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message)
        }
    }

    const handleCancel = () => {
        if (originalSubstance) setSubstance(JSON.parse(JSON.stringify(originalSubstance)))
        setHasChanges(false)
    }

    const handleEdit = () => navigate(`/hazardous-substances/${substanceId}/edit`)
    const handleBack = () => navigate('/hazardous-substances')

    if (loading) return <Typography>Загрузка...</Typography>
    if (!substance) return <Typography>Не найдено</Typography>

    return (
        <Box sx={{ maxWidth: 1200 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isNew ? 'Новое опасное вещество' :
                    isEditMode ? 'Редактирование опасного вещества' : 'Просмотр опасного вещества'}
            </Typography>

            <Paper sx={{ p: 3, mb: 2 }}>
                {/* === ОСНОВНАЯ ИНФОРМАЦИЯ === */}
                <AccordionSection
                    title="Основная информация"
                    open={mainOpen}
                    setOpen={setMainOpen}
                    isEditMode={isEditMode}
                >
                    <Stack spacing={2} sx={{ pl: 2 }}>
                        <TextField
                            label="Наименование"
                            value={substance.name}
                            onChange={e => handleChange('name', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Сокращённое Наименование"
                            value={substance.nameShort}
                            onChange={e => handleChange('nameShort', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                    </Stack>
                </AccordionSection>

                <Divider sx={{ my: 3 }} />

                {/* === ПАРАМЕТРЫ === */}
                <AccordionSection
                    title="Параметры опасности"
                    open={paramsOpen}
                    setOpen={setParamsOpen}
                    isEditMode={isEditMode}
                >
                    {substance.values.length > 0 ? (
                        <TableContainer>
                            <Table size="small" sx={{ '& .MuiTableCell-root':
                                    { verticalAlign: 'top', whiteSpace: 'normal' } }}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell >№</TableCell>
                                        <TableCell >Раздел</TableCell>
                                        <TableCell style={{ width: '26%' }}>Параметр</TableCell>
                                        <TableCell style={{ width: '26%' }}>Значение</TableCell>
                                        <TableCell style={{ width: '26%' }}>Источник</TableCell>
                                        {isEditMode && <TableCell></TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {substance.values.map((param, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{index + 1}</TableCell>
                                            <TableCell >
                                                <TextField
                                                    value={param.sectionNo}
                                                    onChange={e => handleParamChange(index, 'sectionNo', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell style={{ width: '23%' }}>
                                                <TextField
                                                    value={param.title}
                                                    onChange={e => handleParamChange(index, 'title', e.target.value)}
                                                    size="small"
                                                    multiline
                                                    minRows={1}
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell style={{ width: '23%' }}>
                                                <TextField
                                                    value={param.valueText}
                                                    onChange={e => handleParamChange(index, 'valueText', e.target.value)}
                                                    size="small"
                                                    multiline
                                                    minRows={1}
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell style={{ width: '23%' }}>
                                                <TextField
                                                    value={param.sourceInfo}
                                                    onChange={e => handleParamChange(index, 'sourceInfo', e.target.value)}
                                                    size="small"
                                                    multiline
                                                    minRows={1}
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            {isEditMode && (
                                                <TableCell style={{ width: 30 }}>
                                                    <IconButton onClick={() => removeParam(index)} color="error" size="small">
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
                        <Typography color="text.secondary">Параметры не добавлены</Typography>
                    )}
                </AccordionSection>
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