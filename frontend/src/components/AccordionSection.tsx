import { Box, Collapse, Typography } from '@mui/material'
import { ExpandMore, ExpandLess } from '@mui/icons-material'
import * as React from 'react'

interface Props {
    title: string
    open: boolean
    setOpen: (v: boolean) => void
    children: React.ReactNode
    addButton?: React.ReactNode
    isEditMode: boolean
}

export default function AccordionSection({
                                             title,
                                             open,
                                             setOpen,
                                             children,
                                             addButton,
                                             isEditMode
                                         }: Props) {
    return (
        <>
            <Box
                sx={{ bgcolor: '#f5f5f5', borderRadius: 1, cursor: 'pointer', mt: 2 }}
                onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                    const target = e.target as HTMLElement
                    if (target.closest('input, textarea, button, select, .MuiButtonBase-root')) return
                    setOpen(!open)
                }}
            >
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1 }}>
                    <Typography variant="h6">{title}</Typography>
                    {open ? <ExpandLess /> : <ExpandMore />}
                </Box>
            </Box>
            <Collapse in={open}>
                <Box sx={{ p: 2 }}>
                    {children}
                    {isEditMode && addButton}
                </Box>
            </Collapse>
        </>
    )
}