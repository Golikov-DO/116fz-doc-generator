import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from "../../auth/useAuth";
import logo from '../../assets/logo.png'
import './Header.css'

export function Header() {
    const navigate = useNavigate();

    const { user, logout } = useAuth();

    const handleLogout = async () => {
        await logout();
        navigate("/", { replace: true });
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