import { useEffect, useState } from 'react';
import { Paper, Stack, TextField, Typography, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

interface UserData {
    id: number;
    login: string;
    email: string;
}

export default function ProfilePage() {
    const navigate = useNavigate();
    const [data, setData] = useState<UserData | null>(null);
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/users/me', { credentials: 'include' })
            .then(res => {
                if (!res.ok) throw new Error();
                return res.json();
            })
            .then(setData)
            .catch(() => alert('Ошибка загрузки'))
            .finally(() => setLoading(false));
    }, []);

    const handleSave = async () => {
        if (!data) return;

        // Проверка пароля
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
            login: data.login,
            email: data.email
        };
        if (password && password.length > 0) {
            body.password = password;
        }

        const res = await fetch('/api/users/me', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
            credentials: 'include'
        });

        if (!res.ok) {
            alert('Ошибка сохранения');
            return;
        }
        alert('Сохранено');
        window.location.reload();
    };

    if (loading) return <Typography>Загрузка...</Typography>;
    if (!data) return <Typography>Ошибка загрузки</Typography>;

    return (
        <Box sx={{ maxWidth: 600 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                Личный кабинет
            </Typography>

            <Paper sx={{ p: 3 }}>
                <Stack spacing={2}>
                    <TextField
                        label="Логин"
                        value={data.login}
                        onChange={e => setData({...data, login: e.target.value})}
                        fullWidth
                    />
                    <TextField
                        label="Email"
                        value={data.email}
                        onChange={e => setData({...data, email: e.target.value})}
                        fullWidth
                    />
                    <TextField
                        label="Новый пароль (оставьте пустым если не меняете)"
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        fullWidth
                        helperText="Минимум 8 символов, буквы и цифры"
                    />
                </Stack>

                <Box sx={{ display: 'flex', gap: 2, mt: 3, justifyContent: 'center' }}>
                    <Button variant="contained" color="success" onClick={handleSave}>
                        Сохранить
                    </Button>
                    <Button variant="outlined" onClick={() => navigate('/organizations')}>
                        Назад
                    </Button>
                </Box>
            </Paper>
        </Box>
    );
}