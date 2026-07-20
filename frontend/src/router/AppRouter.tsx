import {BrowserRouter, Route, Routes, Navigate} from "react-router-dom";
import ProtectedRoute from "../components/common/ProtectedRoute"
import {MainLayout} from "../components/layout/MainLayout.tsx";
import {HomeLayout} from "../components/layout/HomeLayout.tsx";
import {HomePage} from "../pages/Home/HomePage";
import LoginPage from "../pages/Login/LoginPage";
import AdminPage from "../pages/Admin/AdminPage";
import UserPage from "../pages/Admin/UserPage";
import ProfilePage from "../pages/Profile/ProfilePage";
import OrganizationsPage from "../pages/Organizations/OrganizationsPage";
import OrganizationPage from "../pages/Organization/OrganizationPage";
import ObjectsPage from "../pages/Objects/ObjectsPage";
import ObjectPage from '../pages/Object/ObjectPage';
import AllObjectsPage from "../pages/Objects/AllObjectsPage.tsx";
import AsfsPage from "../pages/Asf/AsfsPage.tsx";
import AsfPage from "../pages/Asf/AsfPage.tsx";

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
                    <Route path="admin" element={
                        <ProtectedRoute>
                            <AdminPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="admin/users/:id" element={
                        <ProtectedRoute>
                            <UserPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="admin/users/:id/edit" element={
                        <ProtectedRoute>
                            <UserPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="profile" element={
                        <ProtectedRoute>
                            <ProfilePage/>
                        </ProtectedRoute>
                    }/>
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

                    <Route path="objects" element={
                        <ProtectedRoute>
                            <AllObjectsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="objects/:orgId" element={
                        <ProtectedRoute>
                            <ObjectsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="/organizations/:orgId/objects/:id" element={<ObjectPage />} />
                    <Route path="/organizations/:orgId/objects/:id/edit" element={<ObjectPage />} />
                    <Route path="/organizations/:orgId/objects/new" element={<ObjectPage />} />
                    <Route path="/asfs" element={
                        <ProtectedRoute>
                            <AsfsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="/asf/:id" element={<AsfPage />} />
                    <Route path="/asf/:id/edit" element={<AsfPage />} />
                    <Route path="/asf/new" element={<AsfPage />} />
                </Route>

                {/* ВСЁ ОСТАЛЬНОЕ — редирект на главную */}
                <Route path="*" element={<Navigate to="/" replace/>}/>

            </Routes>
        </BrowserRouter>
    );
}