import { NavLink, useLocation } from "react-router-dom";
import { useAuth } from '../../auth/useAuth.ts';
import { useState } from "react";
import { ChevronDown, ChevronRight } from "lucide-react";
import './Sidebar.css';

interface MenuItem {
    to?: string;
    label: string;
    children?: MenuItem[];
}

export default function Sidebar() {
    const { user, isAdmin } = useAuth();
    const location = useLocation();

    const [expanded, setExpanded] = useState<Record<string, boolean>>({
        reference: location.pathname.startsWith("/hazardous-substances") ||
            location.pathname.startsWith("/scenarios") ||
            location.pathname.startsWith("/regions") ||
            location.pathname.startsWith("/types"),
        serviceTables: location.pathname.startsWith("/table-titles") ||
            location.pathname.startsWith("/emergency-services")
    });

    const toggle = (key: string) => {
        setExpanded(prev => ({ ...prev, [key]: !prev[key] }));
    };

    const mainLinks: MenuItem[] = [
        { to: "/organizations", label: "Организации" },
        { to: "/objects", label: "Объекты" },
        { to: "/asfs", label: "Список ПАСФ" },
    ];

    const referenceChildren: MenuItem[] = [
        { to: "/hazardous-substances", label: "Опасное вещество" },
        { to: "/scenarios", label: "Сценарии" },
        { to: "/cities", label: "Регион расположения" },
        { to: "/types", label: "Тип объекта" },
    ];

    const serviceTablesChildren: MenuItem[] = [
        { to: "/table-titles", label: "Заголовки таблиц Приложения 2" },
        { to: "/emergency-services", label: "Экстренные службы" },
    ];

    const isReferenceActive = location.pathname.startsWith("/hazardous-substances") ||
        location.pathname.startsWith("/scenarios") ||
        location.pathname.startsWith("/cities") ||
        location.pathname.startsWith("/types");

    const isServiceTablesActive = location.pathname.startsWith("/table-titles") ||
        location.pathname.startsWith("/emergency-services");

    return (
        <aside className="sidebar">
            <nav>
                {mainLinks.map((link) => (
                    <NavLink
                        key={link.to}
                        to={link.to!}
                        className={({ isActive }) =>
                            isActive ? 'sidebar-link active' : 'sidebar-link'
                        }
                    >
                        {link.label}
                    </NavLink>
                ))}

                {/* === СПРАВОЧНЫЕ ДАННЫЕ === */}
                <div className="sidebar-group">
                    <button
                        className={`sidebar-link sidebar-group-toggle ${isReferenceActive ? 'active' : ''}`}
                        onClick={() => toggle("reference")}
                        style={{
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "space-between",
                            width: "100%",
                            background: "none",
                            border: "none",
                            cursor: "pointer",
                            fontSize: "inherit",
                            fontFamily: "inherit",
                            padding: "12px 16px",
                            color: "inherit",
                            textAlign: "left"
                        }}
                    >
                        <span>Справочные данные</span>
                        {expanded.reference ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                    </button>
                    {expanded.reference && (
                        <div className="sidebar-children" style={{ paddingLeft: 16 }}>
                            {referenceChildren.map((child) => (
                                <NavLink
                                    key={child.to}
                                    to={child.to!}
                                    className={({ isActive }) =>
                                        isActive ? 'sidebar-link active' : 'sidebar-link'
                                    }
                                >
                                    {child.label}
                                </NavLink>
                            ))}
                        </div>
                    )}
                </div>

                {/* === СЛУЖЕБНЫЕ ТАБЛИЦЫ (только для admin) === */}
                {(isAdmin || user?.login === 'admin') && (
                    <div className="sidebar-group">
                        <button
                            className={`sidebar-link sidebar-group-toggle ${isServiceTablesActive ? 'active' : ''}`}
                            onClick={() => toggle("serviceTables")}
                            style={{
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "space-between",
                                width: "100%",
                                background: "none",
                                border: "none",
                                cursor: "pointer",
                                fontSize: "inherit",
                                fontFamily: "inherit",
                                padding: "12px 16px",
                                color: "inherit",
                                textAlign: "left"
                            }}
                        >
                            <span>Служебные таблицы</span>
                            {expanded.serviceTables ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                        </button>
                        {expanded.serviceTables && (
                            <div className="sidebar-children" style={{ paddingLeft: 16 }}>
                                {serviceTablesChildren.map((child) => (
                                    <NavLink
                                        key={child.to}
                                        to={child.to!}
                                        className={({ isActive }) =>
                                            isActive ? 'sidebar-link active' : 'sidebar-link'
                                        }
                                    >
                                        {child.label}
                                    </NavLink>
                                ))}
                            </div>
                        )}
                    </div>
                )}

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