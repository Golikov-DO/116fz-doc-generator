// src/layouts/HomeLayout.tsx
import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import './HomeLayout.css';

export function HomeLayout() {
    return (
        <div className="home-layout">
            <Header />
            <main className="home-content">
                <Outlet />
            </main>
        </div>
    );
}