import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

export default function LoginPage() {
    const navigate = useNavigate();
    const [isLogin, setIsLogin] = useState(true);
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [loginStatus, setLoginStatus] = useState<'idle' | 'free' | 'taken'>('idle');

    // Проверка логина при регистрации (твой эндпоинт)
    const checkLogin = async (value: string) => {
        if (value.length < 3) {
            setLoginStatus('idle');
            return;
        }
        try {
            const res = await fetch(
                `/api/auth/check-login?login=${encodeURIComponent(value)}`);
            const data = await res.json();  // ← тут JSON, не text!
            setLoginStatus(data.available ? 'free' : 'taken');
        } catch {
            setLoginStatus('idle');
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        if (isLogin) {
            // === ВХОД через Spring Security ===
            const formData = new URLSearchParams();
            formData.append('login', login);
            formData.append('password', password);

            try {
                const res = await fetch('/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: formData.toString(),
                    credentials: 'include', // важно! для JSESSIONID cookie
                });

                if (res.ok) {
                    // Успех! Spring создал сессию
                    window.location.href = '/organizations';
                } else {
                    setError('Неверный логин или пароль');
                }
            } catch {
                setError('Ошибка сети');
            }

        } else {
            // === РЕГИСТРАЦИЯ ===
            if (password !== confirmPassword) {
                setError('Пароли не совпадают');
                setIsLoading(false);
                return;
            }

            try {
                const res = await fetch('/api/auth/register', {  // твой контроллер
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ login, password, email }),
                    credentials: 'include',
                });

                if (res.ok) {
                    // Регистрация успешна — переключаем на вход
                    setIsLogin(true);
                    setPassword('');
                    setConfirmPassword('');
                    setEmail('');
                    setLoginStatus('idle');
                    setError('Регистрация успешна! Войдите с новым паролем.');
                } else {
                    const msg = await res.text();
                    setError(msg || 'Ошибка регистрации');
                }
            } catch {
                setError('Ошибка сети');
            }
        }

        setIsLoading(false);
    };

    return (
        <div className="login-overlay">
            <div className="login-card">
                <div className="login-header">
                    <h2>{isLogin ? 'Вход' : 'Регистрация'}</h2>
                    <span className="close-btn" onClick={() => navigate('/')}>×</span>
                </div>

                {error && <div className="error-msg">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Логин</label>
                        <input
                            type="text"
                            value={login}
                            onChange={(e) => {
                                setLogin(e.target.value);
                                if (!isLogin) {
                                    checkLogin(e.target.value);
                                }
                            }}
                            required
                            minLength={3}
                        />
                        {!isLogin && login.length >= 3 && (
                            <span style={{
                                color: loginStatus === 'free' ? 'green' : 'red',
                                fontSize: '12px',
                                marginTop: '4px',
                                display: 'block'
                            }}>
                            {loginStatus === 'free' && '✅ свободен'}
                                {loginStatus === 'taken' && '❌ занят'}
                            </span>
                        )}
                    </div>

                    {!isLogin && (
                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                    )}

                    <div className="form-group">
                        <label>Пароль</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {!isLogin && (
                        <div className="form-group">
                            <label>Подтвердите пароль</label>
                            <input
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                    )}

                    <button
                        type="submit"
                        className="btn-submit"
                        disabled={isLoading || (!isLogin && loginStatus !== 'free')}
                    >
                        {isLogin ? 'Войти' : 'Зарегистрироваться'}
                    </button>
                </form>

                <div className="switch-mode">
                    <button onClick={() => {
                        setIsLogin(!isLogin);
                        setError('');
                        setLoginStatus('idle');
                    }}>
                        {isLogin ? 'Нет аккаунта? Зарегистрироваться' : 'Уже есть аккаунт? Войти'}
                    </button>
                </div>
            </div>
        </div>
    );
}