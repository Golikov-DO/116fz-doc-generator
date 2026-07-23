import { useEffect, useState } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { Paper, Stack, TextField, Typography, Button, Box, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { Edit, ArrowBack } from '@mui/icons-material';

interface UserData {
    id: number;
    login: string;
    email: string;
    role: string;
}

export default function UserPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const userId = Number(id);

    const isEditMode = location.pathname.endsWith('/edit');

    const [user, setUser] = useState<UserData | null>(null);
    const [originalUser, setOriginalUser] = useState<UserData | null>(null);
    const [password, setPassword] = useState('');
    const [hasChanges, setHasChanges] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`/api/users/${userId}`, { credentials: 'include' })
            .then(res => {
                if (!res.ok) throw new Error();
                return res.json();
            })
            .then(data => {
                setUser(data);
                setOriginalUser(JSON.parse(JSON.stringify(data)));
                setLoading(false);
            })
            .catch(() => {
                alert('Ошибка загрузки');
                navigate('/admin');
            });
    }, [userId, navigate]);

    const handleChange = (field: keyof UserData, value: string) => {
        if (!user) return;
        setUser({ ...user, [field]: value });
        setHasChanges(true);
    };

    const handleSave = async () => {
        if (!user) return;

        if (password && password.length > 0) {
            if (password.length < 8) {
                alert('Пароль должен быть минимум 8 символов');
                return;
            }
            if (!/[^0-9]/.test(password) || !/[0-9]/.test(password)) {
                alert('Пароль должен содержать хотя бы одну цифру и хотя бы один другой символ');
                return;
            }
        }

        const body: any = {
            login: user.login,
            email: user.email,
            role: user.role
        };
        if (password && password.length > 0) {
            body.password = password;
        }

        const res = await fetch(`/api/users/${userId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
            credentials: 'include'
        });

        if (!res.ok) {
            alert('Ошибка сохранения');
            return;
        }

        const updated = await res.json();
        setUser(updated);
        setOriginalUser(JSON.parse(JSON.stringify(updated)));
        setHasChanges(false);
        setPassword('');
    };

    const handleCancel = () => {
        if (originalUser) {
            setUser(JSON.parse(JSON.stringify(originalUser)));
        }
        setHasChanges(false);
        setPassword('');
    };

    const handleEdit = () => {
        navigate(`/admin/users/${userId}/edit`);
    };

    const handleBack = () => {
        navigate('/admin');
    };

    if (loading) return <Typography>Загрузка...</Typography>;
    if (!user) return <Typography>Не найдено</Typography>;

    return (
        <Box sx={{ maxWidth: 600 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                {isEditMode ? 'Редактирование пользователя' : 'Просмотр пользователя'}
            </Typography>

            <Paper sx={{ p: 3 }}>
                <Stack spacing={2}>
                    <TextField
                        label="Логин"
                        value={user.login}
                        onChange={e => handleChange('login', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <TextField
                        label="Email"
                        value={user.email}
                        onChange={e => handleChange('email', e.target.value)}
                        fullWidth
                        disabled={!isEditMode}
                    />
                    <FormControl fullWidth disabled={!isEditMode}>
                        <InputLabel>Роль</InputLabel>
                        <Select
                            value={user.role}
                            label="Роль"
                            onChange={e =>
                                handleChange('role', e.target.value)}
                        >
                            <MenuItem value="ADMIN">ADMIN</MenuItem>
                            <MenuItem value="USER">USER</MenuItem>
                        </Select>
                    </FormControl>
                    {isEditMode && (
                        <TextField
                            label="Новый пароль (оставьте пустым если не меняете)"
                            type="password"
                            value={password}
                            onChange={e => {
                                setPassword(e.target.value); setHasChanges(true); }}
                            fullWidth
                            helperText="Минимум 8 символов, буквы и цифры"
                        />
                    )}
                </Stack>

                <Box sx={{ display: 'flex', gap: 2, mt: 3, justifyContent: 'center' }}>
                    {!isEditMode ? (
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
            </Paper>
        </Box>
    );
}