import { Link, useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import logo from '../../assets/logo.png'
import './Header.css'

interface User {
    id: number
    login: string
    email: string
    role: string
}

export function Header() {
    const navigate = useNavigate()
    const [user, setUser] = useState<User | null>(null)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(res => res.ok ? res.json() : null)
            .then(data => setUser(data))
            .catch(() => setUser(null))
    }, [])

    const handleLogout = () => {
        // Просто переходим на /logout — Spring обработает и редиректнет
        window.location.href = '/logout';
    };

    return (
        <header className="header">
            <div className="header-container">
                <Link to="/" className="header-logo">
                    <img src={logo} alt="ЭКОСПАС" className="logo-img" />
                </Link>
                <div className="header-right">
                    {user ? (
                        <>
                            <span>{user.login}</span>
                            <button className="btn-login" onClick={handleLogout}>
                                Выйти
                            </button>
                        </>
                    ) : (
                        <button className="btn-login" onClick={() =>
                            navigate('/login')}>
                            Войти
                        </button>
                    )}
                </div>
            </div>
        </header>
    )
}