// src/router/AppRouter.tsx
import {BrowserRouter, Route, Routes, Navigate} from "react-router-dom";
import ProtectedRoute from "../components/common/ProtectedRoute"
import {MainLayout} from "../layouts/MainLayout";
import {HomeLayout} from "../layouts/HomeLayout";
import {HomePage} from "../pages/Home/HomePage";
import LoginPage from "../pages/Login/LoginPage";
import OrganizationsPage from "../pages/Organizations/OrganizationsPage";
import OrganizationPage from "../pages/Organization/OrganizationPage";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Стартовая страница — без сайдбара */}
                <Route element={<HomeLayout/>}>
                    <Route path="/" element={<HomePage/>}/>
                    <Route path="/login" element={<LoginPage/>}/>
                </Route>

                {/* Внутренние страницы — с сайдбаром */}
                <Route element={<MainLayout/>}>
                    <Route path="organizations" element={
                        <ProtectedRoute>
                            <OrganizationsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="organization/:id" element={
                        <ProtectedRoute>
                            <OrganizationPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="organization/:id/edit" element={
                        <ProtectedRoute>
                            <OrganizationPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="organization/new" element={
                        <ProtectedRoute>
                            <OrganizationPage/>
                        </ProtectedRoute>
                    }/>
                </Route>

                {/* ВСЁ ОСТАЛЬНОЕ — редирект на главную */}
                <Route path="*" element={<Navigate to="/" replace/>}/>

            </Routes>
        </BrowserRouter>
    );
}