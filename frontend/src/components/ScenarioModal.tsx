import { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Checkbox, FormControlLabel } from '@mui/material';

interface Scenario {
    id: number;
    name: string;
}

interface Props {
    open: boolean;
    onClose: () => void;
    scenarios: Scenario[];
    selectedIds: number[];
    onApply: (ids: number[]) => void;
}

export default function ScenarioModal({ open, onClose, scenarios, selectedIds, onApply }: Props) {
    const [localSelected, setLocalSelected] = useState<number[]>(selectedIds);

    const toggle = (id: number) => {
        setLocalSelected(prev =>
            prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
        );
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>Выбор сценариев</DialogTitle>
            <DialogContent>
                {scenarios.map(s => (
                    <FormControlLabel
                        key={s.id}
                        control={
                            <Checkbox
                                checked={localSelected.includes(s.id)}
                                onChange={() => toggle(s.id)}
                            />
                        }
                        label={s.name}
                    />
                ))}
            </DialogContent>
            <DialogActions>
                <Button onClick={() => onApply(localSelected)}>Применить</Button>
                <Button onClick={onClose}>Закрыть</Button>
            </DialogActions>
        </Dialog>
    );
}