import { NavLink } from "react-router-dom";
import { useAuth } from '../../auth/useAuth.ts';
import './Sidebar.css';

export default function Sidebar() {
    const { user, isAdmin } = useAuth();

    const links = [
        { to: "/organizations", label: "Организации" },
        { to: "/objects", label: "Объекты" },
        { to: "/asfs", label: "Список ПАСФ" },
    ];

    return (
        <aside className="sidebar">
            <nav>
                {links.map((link) => (
                    <NavLink
                        key={link.to}
                        to={link.to}
                        className={({ isActive }) =>
                            isActive ? 'sidebar-link active' : 'sidebar-link'
                        }
                    >
                        {link.label}
                    </NavLink>
                ))}
                {isAdmin && (
                    <NavLink
                        to="/admin"
                        className={({ isActive }) =>
                            isActive ? 'sidebar-link active' : 'sidebar-link'
                        }
                    >
                        Админ панель
                    </NavLink>
                )}
                {user && !isAdmin && (
                    <NavLink
                        to="/profile"
                        className={({ isActive }) =>
                            isActive ? 'sidebar-link active' : 'sidebar-link'
                        }
                    >
                        Личный кабинет
                    </NavLink>
                )}
            </nav>
        </aside>
    );
}