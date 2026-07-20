import { Outlet } from 'react-router-dom'
import { Header } from './Header.tsx'
import Sidebar from './Sidebar.tsx'
import './MainLayout.css'

export function MainLayout() {
    return (
        <div className="main-layout">
            <Header />
            <div className="layout-body">
                <Sidebar />
                <main className="main-content">
                    <Outlet />
                </main>
            </div>
        </div>
    )
}