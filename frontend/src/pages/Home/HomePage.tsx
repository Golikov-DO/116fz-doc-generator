import { Link } from 'react-router-dom';
import './Home.css';

export function HomePage() {
    return (
        <div className="home-page">
            <div className="welcome-card">
                <h1>План мероприятий по 116-ФЗ</h1>
                <p className="subtitle">
                    Система управления документацией промышленной безопасности
                </p>
                <Link to="/login" className="btn-primary">
                    Перейти в систему
                </Link>
            </div>

            <div className="features">
                <div className="feature">
                    <div className="icon">📄</div>
                    <h3>Документы</h3>
                    <p>Формирование планов мероприятий в Microsoft Word</p>
                </div>
                <div className="feature">
                    <div className="icon">🏢</div>
                    <h3>Организации</h3>
                    <p>Управление организациями</p>
                </div>
                <div className="feature">
                    <div className="icon">🏭</div>
                    <h3>Объекты</h3>
                    <p>Управление объектами</p>
                </div>
            </div>
        </div>
    );
}