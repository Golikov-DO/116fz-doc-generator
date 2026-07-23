import {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './LoginPage.css';
import {useAuth} from "../../auth/useAuth";
import {login as loginRequest} from "../../api/authApi";

export default function LoginPage() {
    const navigate = useNavigate();
    const {refreshUser} = useAuth();
    const [isLogin, setIsLogin] = useState(true);
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [loginStatus, setLoginStatus] = useState<'idle' | 'free' | 'taken'>('idle');

    // === ПРОВЕРКА ЛОГИНА С ДЕБАУНСОМ ===
    useEffect(() => {
        if (isLogin || login.length < 3) {
            setLoginStatus('idle');
            return;
        }

        const timer = setTimeout(async () => {
            try {
                const res = await fetch(
                    `/api/auth/check-login?login=${encodeURIComponent(login)}`
                );
                const data = await res.json();
                setLoginStatus(data.available ? 'free' : 'taken');
            } catch {
                setLoginStatus('idle');
            }
        }, 300);

        return () => clearTimeout(timer);
    }, [login, isLogin]);

    const handleSubmit =
        async (e: React.SubmitEvent<HTMLFormElement>) => {
            e.preventDefault();
            setError('');
            setIsLoading(true);

            if (isLogin) {
                try {
                    await loginRequest(login, password);
                    await refreshUser();
                    navigate("/organizations", {replace: true});
                } catch (e) {
                    setError("Неверный логин или пароль");
                }
            } else {
                if (password !== confirmPassword) {
                    setError('Пароли не совпадают');
                    setIsLoading(false);
                    return;
                }

                try {
                    const res = await fetch('/api/auth/register', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({login, password, email}),
                        credentials: 'include',
                    });

                    if (res.ok) {
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
                    <span className="close-btn" onClick={() =>
                        navigate('/')}>×</span>
                </div>

                {error && <div className="error-msg">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Логин</label>
                        <input
                            type="text"
                            value={login}
                            onChange={(e) =>
                                setLogin(e.target.value)}  // ← просто setLogin, без async
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
                                onChange={(e) =>
                                    setEmail(e.target.value)}
                                required
                            />
                        </div>
                    )}

                    <div className="form-group">
                        <label>Пароль</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)}
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