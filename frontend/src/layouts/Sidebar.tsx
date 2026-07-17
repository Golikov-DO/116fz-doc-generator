import { NavLink } from "react-router-dom"
import './Sidebar.css'

const links = [
    { to: "/organizations", label: "Организации" },
    { to: "/objects", label: "Объекты" },
    // добавишь остальные позже
]

export default function Sidebar() {
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
            </nav>
        </aside>
    )
}