import { useEffect, useState } from 'react';
import { Typography, Paper, Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, IconButton } from '@mui/material';
import { Delete, Edit, Visibility } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

interface User {
    id: number;
    login: string;
    role: string;
}

interface Stats {
    organizations: number;
    objects: number;
}

export default function AdminPage() {
    const navigate = useNavigate();
    const [users, setUsers] = useState<User[]>([]);
    const [stats, setStats] = useState<Stats | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([
            fetch('/api/users', { credentials: 'include' }).then(r => r.json()),
            fetch('/api/stats', { credentials: 'include' }).then(r => r.json())
        ])
            .then(([usersData, statsData]) => {
                setUsers(usersData);
                setStats(statsData);
            })
            .catch(() => alert('Ошибка загрузки'))
            .finally(() => setLoading(false));
    }, []);

    const handleDelete = async (id: number) => {
        if (!confirm('Удалить пользователя?')) return;
        try {
            await fetch(`/api/users/${id}`, { method: 'DELETE', credentials: 'include' });
            setUsers(users.filter(u => u.id !== id));
        } catch {
            alert('Ошибка удаления');
        }
    };

    if (loading) return <Typography>Загрузка...</Typography>;

    return (
        <Box sx={{ maxWidth: 1000 }}>
            <Typography variant="h5" sx={{ mb: 2, pb: 1, borderBottom: '2px solid #4caf50' }}>
                Админ панель
            </Typography>

            {/* Статистика */}
            <Paper sx={{ p: 2, mb: 3 }}>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell></TableCell>
                            <TableCell>Организаций</TableCell>
                            <TableCell>Объектов</TableCell>
                            <TableCell>ПМЛЛПА</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow>
                            <TableCell><strong>Всего</strong></TableCell>
                            <TableCell>{stats?.organizations || 0}</TableCell>
                            <TableCell>{stats?.objects || 0}</TableCell>
                            <TableCell>—</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </Paper>

            {/* Пользователи */}
            <Typography variant="h6" sx={{ mb: 2 }}>Пользователи</Typography>

            <TableContainer component={Paper}>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>№</TableCell>
                            <TableCell>Логин</TableCell>
                            <TableCell>Роль</TableCell>
                            <TableCell>Действия</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users
                            .sort((a, b) => a.login.localeCompare(b.login))
                            .map((user, index) => (
                            <TableRow key={user.id}>
                                <TableCell>{index + 1}</TableCell>
                                <TableCell>{user.login}</TableCell>
                                <TableCell>{user.role}</TableCell>
                                <TableCell>
                                    <IconButton size="small" onClick={() => navigate(`/admin/users/${user.id}`)}>
                                        <Visibility fontSize="small"/>
                                    </IconButton>
                                    <IconButton size="small" onClick={() => navigate(`/admin/users/${user.id}/edit`)}>
                                        <Edit fontSize="small"/>
                                    </IconButton>
                                    <IconButton size="small" onClick={() => handleDelete(user.id)} color="error">
                                        <Delete fontSize="small"/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Button
                variant="contained"
                sx={{ mt: 2 }}
                onClick={() => navigate('/admin/users/new')}
            >
                Добавить пользователя
            </Button>
        </Box>
    );
}