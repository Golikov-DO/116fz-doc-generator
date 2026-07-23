import { useState } from 'react'
import { Paper, Typography, Box, Button, IconButton, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material'
import { Delete } from '@mui/icons-material'

interface AsfImage {
    id: number
    groupKey: string
    nameDocument: string
}

interface Props {
    asfId: number
    images: AsfImage[]
    isEditMode: boolean
    onImagesChange: (images: AsfImage[]) => void
}

const APP_CONFIG = [
    {
        title: 'ПРИЛОЖЕНИЕ 1',
        groupKey: '1',
        positions: [
            { label: 'Скан Свидетельства лицевая сторона', width: 985, height: 1414 },
            { label: 'Скан Свидетельства оборотная сторона', width: 1047, height: 1480 },
        ]
    },
    {
        title: 'ПРИЛОЖЕНИЕ 2',
        groupKey: '2',
        positions: [
            { label: 'Скан Паспорта первая страница', width: 985, height: 1414 },
            { label: 'Скан Паспорта дополнительные страницы', width: 1047, height: 1480 },
        ]
    }
]

// === Ресайз изображения через Canvas ===
function resizeImage(file: File, targetWidth: number, targetHeight: number): Promise<Blob> {
    return new Promise((resolve, reject) => {
        const img = new Image()
        img.onload = () => {
            const canvas = document.createElement('canvas')
            canvas.width = targetWidth
            canvas.height = targetHeight
            const ctx = canvas.getContext('2d')
            if (!ctx) {
                reject(new Error('Canvas context not available'))
                return
            }
            // Рисуем с сохранением пропорций (cover) или растягиваем (fill)
            // Для документов лучше fill — точные размеры
            ctx.drawImage(img, 0, 0, targetWidth, targetHeight)
            canvas.toBlob((blob) => {
                if (blob) resolve(blob)
                else reject(new Error('Failed to create blob'))
            }, 'image/png')
        }
        img.onerror = () => reject(new Error('Failed to load image'))
        img.src = URL.createObjectURL(file)
    })
}

// === Проверка размеров ===
function checkImageDimensions(file: File): Promise<{ width: number; height: number }> {
    return new Promise((resolve, reject) => {
        const img = new Image()
        img.onload = () => {
            resolve({ width: img.width, height: img.height })
            URL.revokeObjectURL(img.src)
        }
        img.onerror = () => {
            reject(new Error('Failed to read image'))
            URL.revokeObjectURL(img.src)
        }
        img.src = URL.createObjectURL(file)
    })
}

export default function AsfImagesSection({ asfId, images, isEditMode, onImagesChange }: Props) {
    const [imageVersion, setImageVersion] = useState(0)
    const [confirmDialog, setConfirmDialog] = useState<{
        open: boolean
        file: File | null
        groupKey: string
        replaceId?: number
        currentWidth: number
        currentHeight: number
        targetWidth: number
        targetHeight: number
    }>({
        open: false,
        file: null,
        groupKey: '',
        currentWidth: 0,
        currentHeight: 0,
        targetWidth: 0,
        targetHeight: 0
    })

    const getGroupImages = (groupKey: string) =>
        images.filter(img => img.groupKey === groupKey).sort((a, b) => a.id - b.id)

    const doUpload = async (groupKey: string, file: File, replaceId?: number) => {
        const formData = new FormData()
        formData.append('asfId', String(asfId))
        formData.append('group', groupKey)
        formData.append('imageId', replaceId ? String(replaceId) : '0')
        formData.append('file', file)

        try {
            const res = await fetch('/api/asf-images', {
                method: 'POST',
                body: formData,
                credentials: 'include'
            })
            if (!res.ok) {
                alert('Ошибка загрузки')
                return
            }
            const data = await res.json()

            if (replaceId) {
                onImagesChange(images.map(img => img.id === replaceId ? { ...img, id: data.id } : img))
            } else {
                const groupImages = getGroupImages(groupKey)
                const prefix = groupKey === '1' ? 'Свидетельство' : 'Паспорт'
                const newImage: AsfImage = {
                    id: data.id,
                    groupKey,
                    nameDocument: `${prefix} ${groupImages.length + 1}`
                }
                onImagesChange([...images, newImage])
            }
            setImageVersion(v => v + 1)
        } catch (err: any) {
            alert('Ошибка загрузки: ' + err.message)
        }
    }

    const handleFileSelect = async (groupKey: string, file: File | null, replaceId?: number, targetWidth?: number, targetHeight?: number) => {
        if (!file || !asfId) return

        // Если размеры не указаны (например, для дополнительных страниц) — загружаем как есть
        if (!targetWidth || !targetHeight) {
            await doUpload(groupKey, file, replaceId)
            return
        }

        const { width, height } = await checkImageDimensions(file)

        // Если размеры совпадают — загружаем сразу
        if (width === targetWidth && height === targetHeight) {
            await doUpload(groupKey, file, replaceId)
            return
        }

        // Иначе — показываем диалог подтверждения
        setConfirmDialog({
            open: true,
            file,
            groupKey,
            replaceId,
            currentWidth: width,
            currentHeight: height,
            targetWidth,
            targetHeight
        })
    }

    const handleConfirmResize = async () => {
        const { file, groupKey, replaceId, targetWidth, targetHeight } = confirmDialog
        if (!file) return

        try {
            const resizedBlob = await resizeImage(file, targetWidth, targetHeight)
            const resizedFile = new File([resizedBlob], file.name, { type: 'image/png' })
            await doUpload(groupKey, resizedFile, replaceId)
        } catch (err: any) {
            alert('Ошибка изменения размера: ' + err.message)
        }
        setConfirmDialog({ ...confirmDialog, open: false, file: null })
    }

    const handleCancelResize = () => {
        setConfirmDialog({ ...confirmDialog, open: false, file: null })
    }

    const handleUpload = async (groupKey: string, file: File | null, replaceId?: number) => {
        if (!file || !asfId) return
        await doUpload(groupKey, file, replaceId)
    }

    const handleDelete = async (imageId: number) => {
        try {
            await fetch(`/api/asf-images/${imageId}`, {
                method: 'DELETE',
                credentials: 'include'
            })
            onImagesChange(images.filter(img => img.id !== imageId))
            setImageVersion(v => v + 1)
        } catch (err: any) {
            alert('Ошибка удаления: ' + err.message)
        }
    }

    const renderImageCard = (img: AsfImage, imgIndex: number) => (
        <Paper
            key={img.id}
            variant="outlined"
            sx={{ p: 1.5, width: 200, display: 'flex', flexDirection: 'column', alignItems: 'center' }}
        >
            <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%', mb: 1 }}>
                <Typography variant="caption">Изображение {imgIndex + 1}</Typography>
                {isEditMode && (
                    <IconButton size="small" color="error" onClick={() => handleDelete(img.id)} sx={{ p: 0 }}>
                        <Delete fontSize="small" />
                    </IconButton>
                )}
            </Box>

            <Box sx={{
                width: 160, height: 220, display: 'flex', alignItems: 'center', justifyContent: 'center',
                border: '1px solid #e0e0e0', mb: 1, overflow: 'hidden'
            }}>
                <img
                    src={`/api/asf-images/${img.id}?t=${imageVersion}`}
                    alt={img.nameDocument}
                    style={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }}
                />
            </Box>

            {isEditMode && (
                <Button variant="outlined" size="small" component="label" fullWidth>
                    Заменить
                    <input
                        type="file" hidden accept="image/*"
                        onChange={async e => {
                            const file = e.target.files?.[0] || null
                            if (!file) return
                            // Определяем позицию по индексу в группе
                            const groupImages = getGroupImages(img.groupKey)
                            const imgIndex = groupImages.findIndex(i => i.id === img.id)
                            const isFirstPosition = imgIndex === 0
                            if (isFirstPosition) {
                                const app = APP_CONFIG.find(a => a.groupKey === img.groupKey)!
                                await handleFileSelect(img.groupKey, file, img.id, app.positions[0].width, app.positions[0].height)
                            } else {
                                await handleUpload(img.groupKey, file, img.id)
                            }
                        }}
                    />
                </Button>
            )}
        </Paper>
    )

    const renderAddButton = (groupKey: string, positionIndex: number) => {
        const app = APP_CONFIG.find(a => a.groupKey === groupKey)!
        const pos = app.positions[positionIndex]
        const isFirstPosition = positionIndex === 0

        return (
            <Paper variant="outlined" sx={{
                p: 1.5, width: 200, display: 'flex', alignItems: 'center', justifyContent: 'center',
                minHeight: 280, borderStyle: 'dashed'
            }}>
                <Button variant="outlined" size="small" component="label" fullWidth>
                    + Добавить
                    <input
                        type="file" hidden accept="image/*"
                        onChange={async e => {
                            const file = e.target.files?.[0] || null
                            if (!file) return
                            if (isFirstPosition) {
                                await handleFileSelect(groupKey, file, undefined, pos.width, pos.height)
                            } else {
                                await handleUpload(groupKey, file)
                            }
                        }}
                    />
                </Button>
            </Paper>
        )
    }

    return (
        <>
            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 3 }}>
                {APP_CONFIG.map(app => {
                    const groupImages = getGroupImages(app.groupKey)

                    return (
                        <Paper key={app.groupKey} variant="outlined" sx={{ p: 2 }}>
                            <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
                                {app.title}
                            </Typography>

                            {/* Позиция 1 (лицевая / первая страница) */}
                            <Box sx={{ mb: 3 }}>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                                    <Typography variant="subtitle1">{app.positions[0].label}</Typography>
                                    <Typography variant="caption" sx={{ color: 'text.secondary' }}>
                                        {app.positions[0].width}x{app.positions[0].height}px
                                    </Typography>
                                </Box>
                                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
                                    {groupImages[0] ? (
                                        renderImageCard(groupImages[0], 0)
                                    ) : isEditMode ? (
                                        renderAddButton(app.groupKey, 0)
                                    ) : (
                                        <Typography color="text.secondary">Нет изображения</Typography>
                                    )}
                                </Box>
                            </Box>

                            {/* Позиция 2 (оборотная / дополнительные) */}
                            <Box>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                                    <Typography variant="subtitle1">{app.positions[1].label}</Typography>
                                    <Typography variant="caption" sx={{ color: 'text.secondary' }}>
                                        {app.positions[1].width}x{app.positions[1].height}px
                                    </Typography>
                                </Box>
                                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
                                    {groupImages.slice(1).map((img, idx) =>
                                        renderImageCard(img, idx)
                                    )}
                                    {isEditMode && renderAddButton(app.groupKey, 1)}
                                </Box>
                            </Box>
                        </Paper>
                    )
                })}
            </Box>

            {/* Диалог подтверждения ресайза */}
            <Dialog open={confirmDialog.open} onClose={handleCancelResize}>
                <DialogTitle>Несоответствие размеров изображения</DialogTitle>
                <DialogContent>
                    <Typography>
                        Текущие размеры: <strong>{confirmDialog.currentWidth}x{confirmDialog.currentHeight}px</strong>
                    </Typography>
                    <Typography sx={{ mt: 1 }}>
                        Требуемые размеры: <strong>{confirmDialog.targetWidth}x{confirmDialog.targetHeight}px</strong>
                    </Typography>
                    <Typography sx={{ mt: 2, color: 'warning.main' }}>
                        Изображение будет автоматически изменено до требуемых размеров. Продолжить?
                    </Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCancelResize}>Отмена</Button>
                    <Button onClick={handleConfirmResize} variant="contained" color="primary">
                        Изменить и загрузить
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    )
}