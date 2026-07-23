// src/pages/Object/ObjectPage.tsx
import {useEffect, useState} from 'react'
import {useNavigate, useLocation, useParams} from 'react-router-dom'
import {
    Paper, Stack, TextField, Typography, Box,
    Button, Table, TableBody, TableCell,
    TableContainer, TableHead, TableRow, IconButton,
    Divider, Select, MenuItem, FormControl, InputLabel
} from '@mui/material'
import type {SelectChangeEvent} from '@mui/material'
import {
    Delete, Add, Edit, ArrowBack
} from '@mui/icons-material'
import {
    getObject, createObject, updateObject, getTypes, getHazardousSubstances,
    getCities, getAsfList, getAsfWithSigners, setPrimarySigner
} from '../../api/objectApi'
import type {
    ObjectFull, ObjectAddress, ObjectInsurancePolicy, ObjectOrderMinimumBalance,
    ObjectCompositionKchs, ObjectPersonsResponsible, ObjectFireEquipment,
    ObjectTechnologicalEquipment, ObjectTechnologicalBlock, ObjectStructure, ObjectImage,
    TypeItem, ObjectScenario, HazardousSubstanceItem, CityItem, AsfItem, AsfSigner
} from '../../types/object'
import AccordionSection from '../../components/AccordionSection'
import ScenarioModal from '../../components/ScenarioModal'

const HAZARD_CLASSES = [
    {value: 1, label: 'I класс'},
    {value: 2, label: 'II класс'},
    {value: 3, label: 'III класс'},
    {value: 4, label: 'IV класс'},
];

const HOURS = Array.from({length: 25}, (_, i) => i);
const MINUTES = Array.from({length: 12}, (_, i) => i * 5);

const IMAGE_GROUPS = [
    {key: 1, title: 'План схема ОПО', required: true},
    {key: 2, title: 'Схема размещения оборудования на объекте', required: false},
    {key: 3, title: 'Схема сценариев развития аварий', required: true},
    {key: 4, title: 'Схема взаимодействия и оповещения при возникновении ЧС', required: true},
];

const IMAGE_TEMPLATES: Record<number, {caption: string, linkText: string}> = {
    1: {caption: 'План схема ОПО', linkText: 'Расположение ОПО приведено на рисунке'},
    2: {caption: 'Схема размещения оборудования на объекте', linkText: 'Размещение оборудования показано на рисунке'},
    3: {caption: 'Схема сценариев развития аварий на ОПО с указанием основных причин их возникновения при разгерметизации оборудования', linkText: 'Схемы сценариев развития аварий с указанием основных причин их возникновения применительно к технологическому оборудованию ОПО приведены на рисунке'},
    4: {caption: 'Схема взаимодействия и оповещения при возникновении ЧС', linkText: 'Схема взаимодействия и оповещения ПАСФ при возникновении ЧС приведена на рисунке'},
};

const EMPTY_ADDRESS: ObjectAddress = {
    addressIndex: '',
    constituentEntity: '',
    areaHierarchy: '',
    city: '',
    street: '',
    house: '',
    coordinates: ''
};

const EMPTY_POLICY: ObjectInsurancePolicy = {
    number: '',
    validUntil: ''
};

const EMPTY_BALANCE: ObjectOrderMinimumBalance = {
    number: '',
    date: ''
};

const EMPTY_OBJECT: ObjectFull = {
    organizationId: 0,
    asfId: null,
    cityId: null,
    typeId: null,
    hazardousSubstanceId: null,
    asfSignerId: null,
    hazardClass: 3,
    objectFullName: '',
    amountOfHazardousSubstance: '',
    nearestFireStation: '',
    departmentGoChsCity: '',
    emergencyCommission: false,
    arrivalTime: '00:00:00',
    address: {...EMPTY_ADDRESS},
    insurancePolicy: {...EMPTY_POLICY},
    minimumBalance: {...EMPTY_BALANCE},
    compositionKchs: [],
    responsiblePersons: [],
    fireEquipments: [],
    technologicalEquipments: [],
    technologicalBlocks: [],
    structures: [],
    images: []
};

export default function ObjectPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const {orgId: orgIdParam, id: idParam} = useParams<{ orgId: string; id: string }>();

    const [obj, setObj] = useState<ObjectFull | null>(null);
    const [originalObj, setOriginalObj] = useState<ObjectFull | null>(null);
    const [hasChanges, setHasChanges] = useState(false);
    const [loading, setLoading] = useState(true);
    const [imageVersion, setImageVersion] = useState(0);

    // Справочники
    const [types, setTypes] = useState<TypeItem[]>([]);
    const [substances, setSubstances] = useState<HazardousSubstanceItem[]>([]);
    const [cities, setCities] = useState<CityItem[]>([]);
    const [asfList, setAsfList] = useState<AsfItem[]>([]);
    const [asfSigners, setAsfSigners] = useState<AsfSigner[]>([]);

    // Состояния раскрытия секций
    const [kchsOpen, setKchsOpen] = useState(false);
    const [structuresOpen, setStructuresOpen] = useState(false);
    const [techBlocksOpen, setTechBlocksOpen] = useState(false);
    const [techEquipOpen, setTechEquipOpen] = useState(false);
    const [fireEquipOpen, setFireEquipOpen] = useState(false);
    const [personsOpen, setPersonsOpen] = useState(false);
    const [imagesOpen, setImagesOpen] = useState(false);

    const [scenarioModalOpen, setScenarioModalOpen] = useState(false);
    const [currentStructureIndex, setCurrentStructureIndex] = useState<number | null>(null);
    const [currentScenarioType, setCurrentScenarioType] = useState<'probable' | 'dangerous' | null>(null);
    const [allScenarios, setAllScenarios] = useState<ObjectScenario[]>([]);

    const isNew = !idParam || idParam === 'new';
    const orgId = Number(orgIdParam) || 0;
    const objId = isNew ? 0 : Number(idParam);

    const isEditMode = location.pathname.endsWith('/edit') || isNew;

    // Загрузка справочников
    useEffect(() => {
        Promise.all([
            getTypes().catch(() => [] as TypeItem[]),
            getHazardousSubstances().catch(() => [] as HazardousSubstanceItem[]),
            getCities().catch(() => [] as CityItem[]),
            getAsfList().catch(() => [] as AsfItem[]),
        ]).then(([typesData, substancesData, citiesData, asfData]) => {
            setTypes(typesData);
            setSubstances(substancesData as HazardousSubstanceItem[]);
            setCities(citiesData);
            setAsfList(asfData);
        });
    }, []);
    useEffect(() => {
        fetch('/api/scenarios', { credentials: 'include' })
            .then(r => r.json())
            .then(setAllScenarios)
            .catch(() => setAllScenarios([]));
    }, []);

    // Загрузка подписантов при смене АСФ
    useEffect(() => {
        if (obj?.asfId && obj.asfId > 0) {
            getAsfWithSigners(obj.asfId)
                .then(data => setAsfSigners(data.signers || []))
                .catch(() => setAsfSigners([]));
        } else {
            setAsfSigners([]);
        }
    }, [obj?.asfId]);

    // Загрузка объекта
    useEffect(() => {
        if (isNew) {
            setObj({
                ...EMPTY_OBJECT,
                organizationId: orgId,
            });
            setOriginalObj(null);
            setLoading(false);
            return;
        }
        getObject(orgId, objId)
            .then(data => {
                const fullData: ObjectFull = {
                    ...EMPTY_OBJECT,
                    ...data,
                    address: data.address || {...EMPTY_ADDRESS},
                    insurancePolicy: data.insurancePolicy || {...EMPTY_POLICY},
                    minimumBalance: data.minimumBalance || {...EMPTY_BALANCE},
                    compositionKchs: data.compositionKchs || [],
                    responsiblePersons: data.responsiblePersons || [],
                    fireEquipments: data.fireEquipments || [],
                    technologicalEquipments: data.technologicalEquipments || [],
                    technologicalBlocks: data.technologicalBlocks || [],
                    structures: (data.structures || []).map((s: any) => ({
                        ...s,
                        likelyIds: s.likelyIds || '',
                        dangerousIds: s.dangerousIds || '',
                        probableScenarioIds: s.likelyIds ? s.likelyIds.split(',').map(Number) : [],
                        dangerousScenarioIds: s.dangerousIds ? s.dangerousIds.split(',').map(Number) : []
                    })),
                    images: data.images || [],
                };
                setObj(fullData);
                setOriginalObj(JSON.parse(JSON.stringify(fullData)));
                setLoading(false);
            })
            .catch(() => {
                alert('Ошибка загрузки объекта');
                navigate(`/organizations/${orgId}/objects`);
            });
    }, [orgId, objId, navigate, isNew]);

    const handleChange = (field: keyof ObjectFull, value: any) => {
        if (!obj) return;
        setObj({...obj, [field]: value});
        setHasChanges(true);
    };

    const handleAddressChange = (field: keyof ObjectAddress, value: string) => {
        if (!obj) return;
        setObj({...obj, address: {...obj.address, [field]: value}});
        setHasChanges(true);
    };

    const handlePolicyChange = (field: keyof ObjectInsurancePolicy, value: string) => {
        if (!obj) return;
        setObj({...obj, insurancePolicy: {...obj.insurancePolicy, [field]: value}});
        setHasChanges(true);
    };

    const handleBalanceChange = (field: keyof ObjectOrderMinimumBalance, value: string) => {
        if (!obj) return;
        setObj({...obj, minimumBalance: {...obj.minimumBalance, [field]: value}});
        setHasChanges(true);
    };

    const handleArrivalTimeChange = (hours: number, minutes: number) => {
        if (!obj) return;
        const timeStr = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:00`;
        setObj({...obj, arrivalTime: timeStr});
        setHasChanges(true);
    };

    // === Таблица: Состав КЧС ===
    const handleKchsChange = (index: number, field: keyof ObjectCompositionKchs, value: string) => {
        if (!obj) return;
        const newList = [...obj.compositionKchs];
        newList[index] = {...newList[index], [field]: value};
        setObj({...obj, compositionKchs: newList});
        setHasChanges(true);
    };
    const addKchs = () => {
        if (!obj) return;
        const newItem: ObjectCompositionKchs = {
            number: obj.compositionKchs.length + 1,
            fullName: '',
            position: '',
            workPhone: '',
            cellPhone: '',
            homeAddress: ''
        };
        setObj({...obj, compositionKchs: [...obj.compositionKchs, newItem]});
        setHasChanges(true);
    };
    const removeKchs = (index: number) => {
        if (!obj) return;
        const newList = obj.compositionKchs.filter((_, i) => i !== index);
        // Перенумеровать
        newList.forEach((item, i) => {
            item.number = i + 1;
        });
        setObj({...obj, compositionKchs: newList});
        setHasChanges(true);
    };

    // === Таблица: Структура объекта ===
    const handleStructureChange = (index: number, field: keyof ObjectStructure, value: string) => {
        if (!obj) return;
        const newList = [...obj.structures];
        newList[index] = {...newList[index], [field]: value};
        setObj({...obj, structures: newList});
        setHasChanges(true);
    };
    const addStructure = () => {
        if (!obj) return;
        const newItem: ObjectStructure = {
            num: obj.structures.length + 1,
            name: '',
            scenarios: [],
            likelyIds: '',
            dangerousIds: '',
            probableScenarioIds: [],
            dangerousScenarioIds: []
        };
        setObj({...obj, structures: [...obj.structures, newItem]});
        setHasChanges(true);
    };
    const removeStructure = (index: number) => {
        if (!obj) return;
        const newList = obj.structures.filter((_, i) => i !== index);
        newList.forEach((item, i) => {
            item.num = i + 1;
        });
        setObj({...obj, structures: newList});
        setHasChanges(true);
    };

    // === Таблица: Технологические блоки ===
    const handleTechBlockChange = (index: number, field: keyof ObjectTechnologicalBlock, value: string) => {
        if (!obj) return;
        const newList = [...obj.technologicalBlocks];
        newList[index] = {...newList[index], [field]: value};
        setObj({...obj, technologicalBlocks: newList});
        setHasChanges(true);
    };
    const addTechBlock = () => {
        if (!obj) return;
        const newItem: ObjectTechnologicalBlock = {
            num: obj.technologicalBlocks.length + 1,
            name: ''
        };
        setObj({...obj, technologicalBlocks: [...obj.technologicalBlocks, newItem]});
        setHasChanges(true);
    };
    const removeTechBlock = (index: number) => {
        if (!obj) return;
        const newList = obj.technologicalBlocks.filter((_, i) => i !== index);
        newList.forEach((item, i) => {
            item.num = i + 1;
        });
        setObj({...obj, technologicalBlocks: newList});
        setHasChanges(true);
    };

    // === Таблица: Технологическое оборудование ===
    const handleTechEquipChange = (index: number, field: keyof ObjectTechnologicalEquipment, value: string) => {
        if (!obj) return;
        const newList = [...obj.technologicalEquipments];
        newList[index] = {...newList[index], [field]: value};
        setObj({...obj, technologicalEquipments: newList});
        setHasChanges(true);
    };
    const addTechEquip = () => {
        if (!obj) return;
        const newItem: ObjectTechnologicalEquipment = {
            num: obj.technologicalEquipments.length + 1,
            name: '',
            characteristics: ''
        };
        setObj({...obj, technologicalEquipments: [...obj.technologicalEquipments, newItem]});
        setHasChanges(true);
    };
    const removeTechEquip = (index: number) => {
        if (!obj) return;
        const newList = obj.technologicalEquipments.filter((_, i) => i !== index);
        newList.forEach((item, i) => {
            item.num = i + 1;
        });
        setObj({...obj, technologicalEquipments: newList});
        setHasChanges(true);
    };

    // === Таблица: Первичные средства пожаротушения ===
    const handleFireEquipChange = (index: number, field: keyof ObjectFireEquipment, value: string) => {
        if (!obj) return;
        const newList = [...obj.fireEquipments];
        newList[index] = {...newList[index], [field]: value};
        setObj({...obj, fireEquipments: newList});
        setHasChanges(true);
    };
    const addFireEquip = () => {
        if (!obj) return;
        const newItem: ObjectFireEquipment = {
            number: obj.fireEquipments.length + 1,
            productName: '',
            quantity: '',
            location: ''
        };
        setObj({...obj, fireEquipments: [...obj.fireEquipments, newItem]});
        setHasChanges(true);
    };
    const removeFireEquip = (index: number) => {
        if (!obj) return;
        const newList = obj.fireEquipments.filter((_, i) => i !== index);
        newList.forEach((item, i) => {
            item.number = i + 1;
        });
        setObj({...obj, fireEquipments: newList});
        setHasChanges(true);
    };

    // === Таблица: Ответственные за план ===
    const handlePersonChange = (index: number, field: keyof ObjectPersonsResponsible, value: string) => {
        if (!obj) return;
        const newList = [...obj.responsiblePersons];
        newList[index] = {...newList[index], [field]: value};
        setObj({...obj, responsiblePersons: newList});
        setHasChanges(true);
    };
    const addPerson = () => {
        if (!obj) return;
        const newItem: ObjectPersonsResponsible = {
            number: obj.responsiblePersons.length + 1,
            fullName: '',
            position: ''
        };
        setObj({...obj, responsiblePersons: [...obj.responsiblePersons, newItem]});
        setHasChanges(true);
    };
    const removePerson = (index: number) => {
        if (!obj) return;
        const newList = obj.responsiblePersons.filter((_, i) => i !== index);
        newList.forEach((item, i) => {
            item.number = i + 1;
        });
        setObj({...obj, responsiblePersons: newList});
        setHasChanges(true);
    };

    // === Таблица: Изображения ===
    const handleImageUpload = async (groupKey: number, file: File | null) => {
        if (!file || !obj) return;

        const formData = new FormData();
        formData.append('objectId', String(obj.id));
        formData.append('group', String(groupKey));
        formData.append('file', file);

        try {
            const res = await fetch('/api/object-images', {
                method: 'POST',
                body: formData,
                credentials: 'include'
            });

            if (!res.ok) {
                alert('Ошибка загрузки')
                return
            }

            const data = await res.json();

            // Обновляем объект — добавляем/обновляем изображение
            const template = IMAGE_TEMPLATES[groupKey];
            const newImage: ObjectImage = {
                id: data.id,
                groupKey: String(groupKey),
                caption: template.caption,
                linkText: template.linkText
            };

            const newImages = obj.images.filter(img => img.groupKey !== String(groupKey));
            newImages.push(newImage);

            setObj({...obj, images: newImages});
            setHasChanges(true);
            setImageVersion(v => v + 1);

        } catch (err: any) {
            alert('Ошибка загрузки изображения: ' + err.message);
        }
    };

    const handleImageDelete = async (groupKey: number) => {
        if (!obj) return;
        const image = obj.images.find(img => img.groupKey === String(groupKey));
        // Удаляем с бэкенда если есть id
        if (image && image.id > 0) {
            try {
                await fetch(`/api/object-images/${image.id}`, {
                    method: 'DELETE',
                    credentials: 'include'
                });
            } catch (err: any) {
                alert('Ошибка удаления изображения: ' + err.message);
                return;
            }
        }

        // Удаляем из локального состояния
        const newImages = obj.images.filter(img => img.groupKey !== String(groupKey));
        setObj({...obj, images: newImages});
        setHasChanges(true);
        setImageVersion(v => v + 1);
    };

    const handleImageCaptionChange = (groupKey: number, value: string) => {
        if (!obj) return;
        const newImages = obj.images.map(img =>
            img.groupKey === String(groupKey) ? {...img, caption: value} : img
        );
        setObj({...obj, images: newImages});
        setHasChanges(true);
    };

    const handleImageLinkChange = (groupKey: number, value: string) => {
        if (!obj) return;
        const newImages = obj.images.map(img =>
            img.groupKey === String(groupKey) ? {...img, linkText: value} : img
        );
        setObj({...obj, images: newImages});
        setHasChanges(true);
    };

    const handleSave = async () => {
        if (!obj) return;
        try {
            if (isNew) {
                const newObj = await createObject(orgId, obj);
                setHasChanges(false);
                navigate(`/organizations/${orgId}/objects/${newObj.id}/edit`);
            } else {
                const objToSave = {
                    ...obj,
                    structures: obj.structures.map(s => ({
                        ...s,
                        likelyIds: s.probableScenarioIds.join(','),
                        dangerousIds: s.dangerousScenarioIds.join(',')
                    }))
                };
                await updateObject(orgId, objId, objToSave);
                setOriginalObj(JSON.parse(JSON.stringify(obj)));
                setHasChanges(false);
            }
        } catch (e: any) {
            alert('Ошибка сохранения: ' + e.message);
        }
    };

    const handleCancel = () => {
        if (originalObj) setObj(JSON.parse(JSON.stringify(originalObj)));
        setHasChanges(false);
    };

    const handleEdit = () => navigate(`/organizations/${orgId}/objects/${objId}/edit`);
    const handleBack = () => navigate(`/objects/${orgId}`);

    const parseArrivalTime = (timeStr: string | null | undefined) => {
        if (!timeStr) return { hours: 0, minutes: 0 }
        const parts = timeStr.split(':')
        return {
            hours: parseInt(parts[0] || '0', 10),
            minutes: parseInt(parts[1] || '0', 10)
        }
    }

    const openScenarioModal = (index: number, type: 'probable' | 'dangerous') => {
        setCurrentStructureIndex(index);
        setCurrentScenarioType(type);
        setScenarioModalOpen(true);
    };

    const applyScenarios = (selectedIds: number[]) => {
        if (currentStructureIndex === null || !currentScenarioType || !obj) return;
        const field = currentScenarioType === 'probable' ? 'probableScenarioIds' : 'dangerousScenarioIds';
        const newStructures = [...obj.structures];
        newStructures[currentStructureIndex] = {
            ...newStructures[currentStructureIndex],
            [field]: selectedIds
        };
        setObj({ ...obj, structures: newStructures });
        setHasChanges(true);
        setScenarioModalOpen(false);
    };

    // === Компонент таблицы с редактированием ===
    if (loading) return <Typography>Загрузка...</Typography>;
    if (!obj) return <Typography>Не найдено</Typography>;

    const arrivalTime = parseArrivalTime(obj.arrivalTime);

    return (
        <Box sx={{maxWidth: 1200}}>
            <Typography variant="h5" sx={{mb: 2, pb: 1, borderBottom: '2px solid #4caf50'}}>
                {isNew ? 'Новый объект' : isEditMode ? 'Редактирование объекта' : 'Просмотр объекта'}
            </Typography>

            <Paper sx={{p: 3, mb: 2}}>
                {/* === ОБЪЕКТ === */}
                <Typography variant="h6" sx={{mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1}}>
                    Объект
                </Typography>

                <Stack spacing={2} sx={{pl: 2}}>
                    {/* Полное наименование */}
                    <TextField
                        label="Полное наименование"
                        value={obj.objectFullName}
                        onChange={e => handleChange('objectFullName', e.target.value)}
                        fullWidth
                        multiline
                        maxRows={4}
                        disabled={!isEditMode}
                    />

                    {/* Ряд: Тип объекта | Опасное вещество | Класс опасности */}
                    <Box sx={{display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 2}}>
                        <FormControl fullWidth disabled={!isEditMode}>
                            <InputLabel>Тип объекта</InputLabel>
                            <Select
                                value={obj.typeId === null ? '' : String(obj.typeId)}
                                onChange={(e: SelectChangeEvent) => handleChange('typeId', e.target.value ? Number(e.target.value) : null)}
                                label="Тип объекта"
                            >
                                <MenuItem value=""><em>Не выбрано</em></MenuItem>
                                {types.map(t => <MenuItem key={t.id} value={t.id}>{t.type}</MenuItem>)}
                            </Select>
                        </FormControl>

                        <FormControl fullWidth disabled={!isEditMode}>
                            <InputLabel>Опасное вещество</InputLabel>
                            <Select
                                value={obj.hazardousSubstanceId === null ? '' : String(obj.hazardousSubstanceId)}
                                onChange={(e: SelectChangeEvent) => handleChange('hazardousSubstanceId', e.target.value ? Number(e.target.value) : null)}
                                label="Опасное вещество"
                            >
                                <MenuItem value=""><em>Не выбрано</em></MenuItem>
                                {substances.map(s => <MenuItem key={s.id} value={s.id}>{s.name}</MenuItem>)}
                            </Select>
                        </FormControl>

                        <FormControl fullWidth disabled={!isEditMode}>
                            <InputLabel>Класс опасности</InputLabel>
                            <Select
                                value={String(obj.hazardClass)}
                                onChange={(e: SelectChangeEvent) => handleChange('hazardClass', Number(e.target.value))}
                                label="Класс опасности"
                            >
                                {HAZARD_CLASSES.map(c => <MenuItem key={c.value} value={c.value}>{c.label}</MenuItem>)}
                            </Select>
                        </FormControl>
                    </Box>

                    {/* Ряд: Наличие КЧС | Количество опасного вещества */}
                    <Box sx={{display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 2}}>
                        <FormControl fullWidth disabled={!isEditMode}>
                            <InputLabel>Наличие КЧС</InputLabel>
                            <Select
                                value={obj.emergencyCommission ? 'true' : 'false'}
                                onChange={(e: SelectChangeEvent) => handleChange('emergencyCommission', e.target.value === 'true')}
                                label="Наличие КЧС"
                            >
                                <MenuItem value="true">Создана</MenuItem>
                                <MenuItem value="false">Не создана</MenuItem>
                            </Select>
                        </FormControl>

                        <TextField
                            label="Количество опасного вещества"
                            value={obj.amountOfHazardousSubstance}
                            onChange={e => handleChange('amountOfHazardousSubstance', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                    </Box>

                    {/* Ряд: Ближайшая ПСЧ | Департамент ГОЧС */}
                    <Box sx={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2}}>
                        <TextField
                            label="Ближайшая ПСЧ"
                            value={obj.nearestFireStation}
                            onChange={e => handleChange('nearestFireStation', e.target.value)}
                            fullWidth
                            multiline
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Департамент ГОЧС"
                            value={obj.departmentGoChsCity}
                            onChange={e => handleChange('departmentGoChsCity', e.target.value)}
                            fullWidth
                            disabled={!isEditMode}
                        />
                    </Box>
                </Stack>

                <Divider sx={{my: 3}}/>

                {/* === ОБСЛУЖИВАЮЩАЯ АСФ === */}
                <Typography variant="h6" sx={{mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1}}>
                    Обслуживающая АСФ
                </Typography>

                <Box sx={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr 200px',
                    gap: 2,
                    pl: 2,
                    alignItems: 'flex-start'
                }}>
                    <FormControl fullWidth disabled={!isEditMode}>
                        <InputLabel>АСФ</InputLabel>
                        <Select
                            value={obj.asfId === null ? '' : String(obj.asfId)}
                            onChange={e => {
                                const val = e.target.value ? Number(e.target.value) : null;
                                setObj({...obj, asfId: val, asfSignerId: null});
                                setHasChanges(true);
                            }}
                            label="АСФ"
                        >
                            <MenuItem value=""><em>Не выбрано</em></MenuItem>
                            {asfList.map(a => <MenuItem key={a.id} value={String(a.id)}>{a.shortName}</MenuItem>)}
                        </Select>
                    </FormControl>

                    <FormControl fullWidth disabled={!isEditMode || !obj.asfId}>
                        <InputLabel>Подписант от АСФ</InputLabel>
                        <Select
                            value={obj.asfSignerId === null ? '' : String(obj.asfSignerId)}
                            onChange={async (e) => {
                                const signerId = e.target.value ? Number(e.target.value) : null;
                                if (signerId && obj?.asfId) {
                                    try {
                                        await setPrimarySigner(obj.asfId, signerId, true);
                                        const newSigners = asfSigners.map(s => ({
                                            ...s,
                                            isPrimary: s.id === signerId
                                        }));
                                        setAsfSigners(newSigners);
                                        handleChange('asfSignerId', signerId);
                                    } catch (err: any) {
                                        alert('Ошибка обновления подписанта: ' + err.message);
                                    }
                                } else {
                                    handleChange('asfSignerId', signerId);
                                }
                            }}
                            label="Подписант от АСФ"
                        >
                            <MenuItem value=""><em>Не выбрано</em></MenuItem>
                            {asfSigners.map(s => (
                                <MenuItem key={s.id} value={String(s.id)}>
                                    {s.name} {s.isPrimary ? '(основной)' : ''}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <Box>
                        <Typography variant="caption" align={"center"}
                                    sx={{display: 'block', mb: 0.5, color: 'text.secondary', fontSize: '0.75rem'}}>
                            Время прибытия
                        </Typography>
                        <Box sx={{display: 'flex', gap: 1}}>
                            <FormControl sx={{width: 100}} disabled={!isEditMode} size="small">
                                <InputLabel>Часы</InputLabel>
                                <Select
                                    value={String(arrivalTime.hours)}
                                    onChange={e => handleArrivalTimeChange(Number(e.target.value), arrivalTime.minutes)}
                                    label="Часы"
                                >
                                    {HOURS.map(h => <MenuItem key={h} value={String(h)}>{h}</MenuItem>)}
                                </Select>
                            </FormControl>
                            <FormControl sx={{width: 100}} disabled={!isEditMode} size="small">
                                <InputLabel>Минуты</InputLabel>
                                <Select
                                    value={String(arrivalTime.minutes)}
                                    onChange={e => handleArrivalTimeChange(arrivalTime.hours, Number(e.target.value))}
                                    label="Минуты"
                                >
                                    {MINUTES.map(m => <MenuItem key={m} value={String(m)}>{m}</MenuItem>)}
                                </Select>
                            </FormControl>
                        </Box>
                    </Box>
                </Box>

                <Divider sx={{my: 3}}/>

                {/* === МЕСТОНАХОЖДЕНИЕ ОБЪЕКТА === */}
                <Typography variant="h6" sx={{mb: 2, bgcolor: '#f5f5f5', p: 1, borderRadius: 1}}>
                    Местонахождение объекта
                </Typography>

                <Box sx={{pl: 2}}>
                    {/* Ряд: Индекс | Субъект РФ | Район | Город */}
                    <Box sx={{display: 'grid', gridTemplateColumns: '120px 1fr 1fr 1fr', gap: 2, mb: 2}}>
                        <TextField
                            label="Индекс"
                            value={obj.address.addressIndex}
                            onChange={e => handleAddressChange('addressIndex', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Субъект РФ"
                            value={obj.address.constituentEntity}
                            onChange={e => handleAddressChange('constituentEntity', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Район"
                            value={obj.address.areaHierarchy}
                            onChange={e => handleAddressChange('areaHierarchy', e.target.value)}
                            multiline
                            maxRows={3}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Город"
                            value={obj.address.city}
                            onChange={e => handleAddressChange('city', e.target.value)}
                            disabled={!isEditMode}
                        />
                    </Box>

                    {/* Ряд: Улица | Дом | Координаты | Район расположения ОПО */}
                    <Box sx={{display: 'grid', gridTemplateColumns: '1.5fr 1fr 1fr 1fr', gap: 2}}>
                        <TextField
                            label="Улица"
                            value={obj.address.street}
                            onChange={e => handleAddressChange('street', e.target.value)}
                            multiline
                            maxRows={3}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Дом"
                            value={obj.address.house}
                            onChange={e => handleAddressChange('house', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <TextField
                            label="Координаты"
                            value={obj.address.coordinates}
                            onChange={e => handleAddressChange('coordinates', e.target.value)}
                            disabled={!isEditMode}
                        />
                        <FormControl fullWidth disabled={!isEditMode}>
                            <InputLabel>Район расположения ОПО</InputLabel>
                            <Select
                                value={obj.cityId === null ? '' : String(obj.cityId)}
                                onChange={e => handleChange('cityId', e.target.value ? Number(e.target.value) : null)}
                                label="Район расположения ОПО"
                            >
                                <MenuItem value=""><em>Не выбрано</em></MenuItem>
                                {cities.map(c => <MenuItem key={c.id} value={String(c.id)}>{c.cityName}</MenuItem>)}
                            </Select>
                        </FormControl>
                    </Box>
                </Box>

                <Divider sx={{my: 3}}/>

                {/* === ПОЛИС И ПРИКАЗ === */}
                <Box sx={{display: 'grid', gridTemplateColumns: '1fr 1fr 1fr 1fr', gap: 2, pl: 2}}>
                    <TextField
                        label="Номер полиса"
                        value={obj.insurancePolicy.number}
                        onChange={e => handlePolicyChange('number', e.target.value)}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Действителен до"
                        type="date"
                        value={obj.insurancePolicy.validUntil}
                        onChange={e => handlePolicyChange('validUntil', e.target.value)}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Номер приказа"
                        value={obj.minimumBalance.number}
                        onChange={e => handleBalanceChange('number', e.target.value)}
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Дата приказа"
                        type="date"
                        value={obj.minimumBalance.date}
                        onChange={e => handleBalanceChange('date', e.target.value)}
                        disabled={!isEditMode}
                    />
                </Box>

                {/* === СОСТАВ КЧС === */}
                {obj.emergencyCommission && (
                    <AccordionSection
                        title="Состав КЧС"
                        open={kchsOpen}
                        setOpen={setKchsOpen}
                        isEditMode={isEditMode}
                        addButton={
                            <Button startIcon={<Add/>} onClick={addKchs}>
                                Добавить
                            </Button>
                        }
                    >
                        <TableContainer>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>№</TableCell>
                                        <TableCell>ФИО</TableCell>
                                        <TableCell>Должность</TableCell>
                                        <TableCell>Раб. телефон</TableCell>
                                        <TableCell>Моб. телефон</TableCell>
                                        <TableCell>Домашний адрес</TableCell>
                                        {isEditMode && <TableCell></TableCell>}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {obj.compositionKchs.map((item, index) => (
                                        <TableRow key={item.id || index}>
                                            <TableCell>{item.number}</TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={item.fullName}
                                                    onChange={e => handleKchsChange(index, 'fullName', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    multiline
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={item.position}
                                                    onChange={e => handleKchsChange(index, 'position', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    multiline
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={item.workPhone || ''}
                                                    onChange={e => handleKchsChange(index, 'workPhone', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={item.cellPhone || ''}
                                                    onChange={e => handleKchsChange(index, 'cellPhone', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={item.homeAddress || ''}
                                                    onChange={e => handleKchsChange(index, 'homeAddress', e.target.value)}
                                                    size="small"
                                                    fullWidth
                                                    multiline
                                                    disabled={!isEditMode}
                                                />
                                            </TableCell>
                                            {isEditMode && (
                                                <TableCell>
                                                    <IconButton onClick={() => removeKchs(index)} color="error"
                                                                size="small">
                                                        <Delete/>
                                                    </IconButton>
                                                </TableCell>
                                            )}
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </AccordionSection>
                )}

                {/* === СТРУКТУРА ОБЪЕКТА === */}
                <AccordionSection
                    title="Структура объекта"
                    open={structuresOpen}
                    setOpen={setStructuresOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add/>} onClick={addStructure}>
                            Добавить
                        </Button>
                    }
                >
                    <TableContainer>
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>№</TableCell>
                                    <TableCell>Наименование</TableCell>
                                    <TableCell>Вероятные сценарии</TableCell>
                                    <TableCell>Опасные сценарии</TableCell>
                                    {isEditMode && <TableCell></TableCell>}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {[...obj.structures].sort((a, b) => a.num - b.num).map((item, index) => (
                                    <TableRow key={item.id || index}>
                                        <TableCell>{item.num}</TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.name}
                                                onChange={e => handleStructureChange(index, 'name', e.target.value)}
                                                size="small"
                                                fullWidth
                                                multiline
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            {item.probableScenarioIds?.map(id => {
                                                const scenario = allScenarios.find(s => s.id === id);
                                                return scenario ? <div key={id}>{scenario.name}</div> : null;
                                            })}
                                            {isEditMode && (
                                                <Button size="small" onClick={() => openScenarioModal(index, 'probable')}>
                                                    Редактировать
                                                </Button>
                                            )}
                                        </TableCell>
                                        <TableCell>
                                            {item.dangerousScenarioIds?.map(id => {
                                                const scenario = allScenarios.find(s => s.id === id);
                                                return scenario ? <div key={id}>{scenario.name}</div> : null;
                                            })}
                                            {isEditMode && (
                                                <Button size="small" onClick={() => openScenarioModal(index, 'dangerous')}>
                                                    Редактировать
                                                </Button>
                                            )}
                                        </TableCell>
                                        {isEditMode && (
                                            <TableCell>
                                                <IconButton onClick={() => removeStructure(index)} color="error"
                                                            size="small">
                                                    <Delete/>
                                                </IconButton>
                                            </TableCell>
                                        )}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </AccordionSection>

                {/* === ТЕХНОЛОГИЧЕСКИЕ БЛОКИ === */}
                <AccordionSection
                    title="Технологические блоки объекта"
                    open={techBlocksOpen}
                    setOpen={setTechBlocksOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add/>} onClick={addTechBlock}>
                            Добавить
                        </Button>
                    }
                >
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
                                {obj.technologicalBlocks.map((item, index) => (
                                    <TableRow key={item.id || index}>
                                        <TableCell>{item.num}</TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.name}
                                                onChange={e => handleTechBlockChange(index, 'name', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        {isEditMode && (
                                            <TableCell>
                                                <IconButton onClick={() => removeTechBlock(index)} color="error"
                                                            size="small">
                                                    <Delete/>
                                                </IconButton>
                                            </TableCell>
                                        )}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </AccordionSection>

                {/* === ТЕХНОЛОГИЧЕСКОЕ ОБОРУДОВАНИЕ === */}
                <AccordionSection
                    title="Технологическое оборудование"
                    open={techEquipOpen}
                    setOpen={setTechEquipOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add/>} onClick={addTechEquip}>
                            Добавить
                        </Button>
                    }
                >
                    <TableContainer>
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>№</TableCell>
                                    <TableCell>Наименование</TableCell>
                                    <TableCell>Характеристики</TableCell>
                                    {isEditMode && <TableCell></TableCell>}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {[...obj.technologicalEquipments].sort((a, b) => a.num - b.num).map((item, index) => (
                                    <TableRow key={item.id || index}>
                                        <TableCell>{item.num}</TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.name}
                                                onChange={e => handleTechEquipChange(index, 'name', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                                multiline
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.characteristics}
                                                onChange={e => handleTechEquipChange(index, 'characteristics', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                                multiline
                                            />
                                        </TableCell>
                                        {isEditMode && (
                                            <TableCell>
                                                <IconButton onClick={() => removeTechEquip(index)} color="error"
                                                            size="small">
                                                    <Delete/>
                                                </IconButton>
                                            </TableCell>
                                        )}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </AccordionSection>

                {/* === ПЕРВИЧНЫЕ СРЕДСТВА ПОЖАРОТУШЕНИЯ === */}
                <AccordionSection
                    title="Первичные средства пожаротушения"
                    open={fireEquipOpen}
                    setOpen={setFireEquipOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add/>} onClick={addFireEquip}>
                            Добавить
                        </Button>
                    }
                >
                    <TableContainer>
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>№</TableCell>
                                    <TableCell>Наименование</TableCell>
                                    <TableCell>Количество</TableCell>
                                    <TableCell>Место нахождения</TableCell>
                                    {isEditMode && <TableCell></TableCell>}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {obj.fireEquipments.map((item, index) => (
                                    <TableRow key={item.id || index}>
                                        <TableCell>{item.number}</TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.productName}
                                                onChange={e => handleFireEquipChange(index, 'productName', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.quantity}
                                                onChange={e => handleFireEquipChange(index, 'quantity', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.location}
                                                onChange={e => handleFireEquipChange(index, 'location', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        {isEditMode && (
                                            <TableCell>
                                                <IconButton onClick={() => removeFireEquip(index)} color="error"
                                                            size="small">
                                                    <Delete/>
                                                </IconButton>
                                            </TableCell>
                                        )}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </AccordionSection>

                {/* === ОТВЕТСТВЕННЫЕ ЗА ПЛАН === */}
                <AccordionSection
                    title="Ответственные за план"
                    open={personsOpen}
                    setOpen={setPersonsOpen}
                    isEditMode={isEditMode}
                    addButton={
                        <Button startIcon={<Add/>} onClick={addPerson}>
                            Добавить
                        </Button>
                    }
                >
                    <TableContainer>
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>№</TableCell>
                                    <TableCell>ФИО</TableCell>
                                    <TableCell>Должность</TableCell>
                                    {isEditMode && <TableCell></TableCell>}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {obj.responsiblePersons.map((item, index) => (
                                    <TableRow key={item.id || index}>
                                        <TableCell>{item.number}</TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.fullName}
                                                onChange={e => handlePersonChange(index, 'fullName', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <TextField
                                                value={item.position}
                                                onChange={e => handlePersonChange(index, 'position', e.target.value)}
                                                size="small"
                                                fullWidth
                                                disabled={!isEditMode}
                                            />
                                        </TableCell>
                                        {isEditMode && (
                                            <TableCell>
                                                <IconButton onClick={() => removePerson(index)} color="error"
                                                            size="small">
                                                    <Delete/>
                                                </IconButton>
                                            </TableCell>
                                        )}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </AccordionSection>

                {/* === ИЗОБРАЖЕНИЯ === */}
                {!isNew ? (
                <AccordionSection
                    title="Изображения"
                    open={imagesOpen}
                    isEditMode={isEditMode}
                    setOpen={setImagesOpen}
                >
                    <Box sx={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2}}>
                        {IMAGE_GROUPS.map(group => {
                            const image = obj.images.find(img => img.groupKey === String(group.key));
                            const hasImage = image && image.id > 0;

                            // В режиме просмотра: показываем только если есть картинка
                            if (!isEditMode && !hasImage) return null;

                            return (
                                <Paper key={group.key} variant="outlined" sx={{p: 2}}>
                                    <Typography variant="subtitle2" sx={{mb: 1}}>
                                        {group.title}
                                        {group.required && ' (обязательный)'}
                                    </Typography>

                                    {/* Превью */}
                                    {hasImage && (
                                        <Box sx={{mb: 2, textAlign: 'center'}}>
                                            <img
                                                src={`/api/object-images/${image.id}?t=${imageVersion}`}
                                                alt={group.title}
                                                style={{maxWidth: '100%', maxHeight: 150, border: '1px solid #ddd'}}
                                            />
                                        </Box>
                                    )}

                                    {/* Загрузка / Заменить / Удалить */}
                                    {isEditMode && (
                                        <Box sx={{mb: 2}}>
                                            <Button
                                                variant="outlined"
                                                size="small"
                                                component="label"
                                            >
                                                {hasImage ? 'Заменить' : 'Загрузить'}
                                                <input
                                                    type="file"
                                                    hidden
                                                    accept="image/png"
                                                    onChange={e => handleImageUpload(group.key, e.target.files?.[0] || null)}
                                                />
                                            </Button>
                                            {hasImage && (
                                                <Button
                                                    variant="outlined"
                                                    size="small"
                                                    color="error"
                                                    sx={{ml: 1}}
                                                    onClick={() => handleImageDelete(group.key)}
                                                >
                                                    Удалить
                                                </Button>
                                            )}
                                        </Box>
                                    )}

                                    {/* Caption */}
                                    <TextField
                                        label="Подпись"
                                        value={image?.caption || ''}
                                        onChange={e => handleImageCaptionChange(group.key, e.target.value)}
                                        fullWidth
                                        size="small"
                                        disabled={!isEditMode}
                                        sx={{mb: 1}}
                                    />

                                    {/* Link text */}
                                    <TextField
                                        label="Ссылка в тексте"
                                        value={image?.linkText || ''}
                                        onChange={e => handleImageLinkChange(group.key, e.target.value)}
                                        fullWidth
                                        size="small"
                                        disabled={!isEditMode}
                                        multiline
                                    />
                                </Paper>
                            );
                        })}
                    </Box>
                </AccordionSection>
                ) : (
                        <Paper sx={{ p: 2, mt: 2 }}>
                            <Typography color="warning.main">
                                Изображения(сканы документов) можно добавить только после сохранения нового объекта.
                            </Typography>
                        </Paper>
                    )}
            </Paper>

            {scenarioModalOpen && currentStructureIndex !== null && obj && (
                <ScenarioModal
                    open={scenarioModalOpen}
                    onClose={() => setScenarioModalOpen(false)}
                    scenarios={allScenarios}
                    selectedIds={currentScenarioType === 'probable'
                        ? obj.structures[currentStructureIndex].probableScenarioIds
                        : obj.structures[currentStructureIndex].dangerousScenarioIds}
                    onApply={applyScenarios}
                />
            )}

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
                ) : !isEditMode ? (
                    <>
                        <Button variant="contained" startIcon={<Edit/>} onClick={handleEdit}>
                            Редактировать
                        </Button>
                        <Button variant="outlined" startIcon={<ArrowBack/>} onClick={handleBack}>
                            Назад к списку
                        </Button>
                    </>
                ) : hasChanges ? (
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
                )}
            </Box>
        </Box>
    );
}