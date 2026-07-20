import {Link, useNavigate} from 'react-router-dom';
import {useAuth} from '../../hooks/useAuth';
import './Home.css';

export function HomePage() {
    const { isAuth } = useAuth();  // ← вместо const auth = useAuth()
    const navigate = useNavigate()

    return (
        <div className="home-page">
            <div className="welcome-card">
                <h1>План мероприятий по 116-ФЗ</h1>
                <p className="subtitle">
                    Система управления документацией промышленной безопасности
                </p>
                {isAuth ? (  // ← вместо auth
                    <button
                        className="btn-primary"
                        onClick={() => navigate('/organizations')}
                    >
                        Перейти в систему
                    </button>
                ) : (
                    <Link to="/login" className="btn-primary">
                        Перейти в систему
                    </Link>
                )}
            </div>

            <div className="features">
                <div className="feature">
                    <div className="icon">📄</div>
                    <h3>Документы</h3>
                    <p>Формирование планов мероприятий в Microsoft Word</p>
                    {isAuth && (  // ← вместо auth
                        <button
                            className="btn-link"
                            onClick={() => navigate('/documents')}
                        >
                            Управление документами
                        </button>
                    )}
                </div>
                <div className="feature">
                    <div className="icon">🏢</div>
                    <h3>Организации</h3>
                    <p>Управление организациями</p>
                    {isAuth && (  // ← вместо auth
                        <button
                            className="btn-link"
                            onClick={() => navigate('/organizations')}
                        >
                            Управление организациями
                        </button>
                    )}
                </div>
                <div className="feature">
                    <div className="icon">🏭</div>
                    <h3>Объекты</h3>
                    <p>Управление объектами</p>
                    {isAuth && (  // ← вместо auth
                        <button
                            className="btn-link"
                            onClick={() => navigate('/objects')}
                        >
                            Управление объектами
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}