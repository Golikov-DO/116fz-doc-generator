import {BrowserRouter, Route, Routes, Navigate} from "react-router-dom";
import ProtectedRoute from "../components/common/ProtectedRoute"
import {MainLayout} from "../components/layout/MainLayout.tsx";
import {HomeLayout} from "../components/layout/HomeLayout.tsx";
import {HomePage} from "../pages/Home/HomePage";
import LoginPage from "../pages/Login/LoginPage";
import AdminPage from "../pages/Admin/AdminPage";
import UserPage from "../pages/Admin/UserPage";
import ProfilePage from "../pages/Profile/ProfilePage";
import OrganizationsPage from "../pages/Organization/OrganizationsPage.tsx";
import OrganizationPage from "../pages/Organization/OrganizationPage";
import ObjectsPage from "../pages/Object/ObjectsPage.tsx";
import ObjectPage from '../pages/Object/ObjectPage';
import AllObjectsTable from "../components/tables/AllObjectsTable.tsx";
import AsfsPage from "../pages/Asf/AsfsPage.tsx";
import AsfPage from "../pages/Asf/AsfPage.tsx";
import HazardousSubstancesPage from "../pages/Hazardous/HazardousSubstancesPage.tsx";
import HazardousSubstancePage from "../pages/Hazardous/HazardousSubstancePage.tsx";
import CitiesPage from "../pages/City/CitiesPage.tsx";
import CityPage from "../pages/City/CityPage.tsx";
import ScenariosPage from "../pages/Scenario/ScenariosPage.tsx";
import ScenarioPage from "../pages/Scenario/ScenarioPage.tsx";
import TypesPage from "../pages/Type/ObjectTypesPage.tsx";
import TypePage from "../pages/Type/ObjectTypePage.tsx";
import TableTitlesPage from '../pages/TableTitles/TableTitlesPage'
import TableTitlePage from '../pages/TableTitles/TableTitlePage'
import EmergencyServicesPage from '../pages/EmergencyService/EmergencyServicesPage'
import EmergencyServicePage from '../pages/EmergencyService/EmergencyServicePage'
import DocumentsPage from '../pages/documents/DocumentsPage.tsx'
import {AuthProvider} from "../auth/AuthContext";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <AuthProvider>
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
                                <AllObjectsTable/>
                            </ProtectedRoute>
                        }/>
                        <Route path="objects/:orgId" element={
                            <ProtectedRoute>
                                <ObjectsPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/organizations/:orgId/objects/:id" element={
                            <ProtectedRoute>
                                <ObjectPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/organizations/:orgId/objects/:id/edit" element={
                            <ProtectedRoute>
                                <ObjectPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/organizations/:orgId/objects/new" element={
                            <ProtectedRoute>
                                <ObjectPage/>
                            </ProtectedRoute>}/>
                        <Route path="/asfs" element={
                            <ProtectedRoute>
                                <AsfsPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/asf/:id" element={
                            <ProtectedRoute>
                                <AsfPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/asf/:id/edit" element={
                            <ProtectedRoute>
                                <AsfPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/asf/new" element={
                            <ProtectedRoute>
                                <AsfPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/hazardous-substances" element={
                            <ProtectedRoute>
                                <HazardousSubstancesPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/hazardous-substances/:id" element={
                            <ProtectedRoute>
                                <HazardousSubstancePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/hazardous-substances/:id/edit" element={
                            <ProtectedRoute>
                                <HazardousSubstancePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/hazardous-substances/new" element={
                            <ProtectedRoute>
                                <HazardousSubstancePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/cities" element={
                            <ProtectedRoute>
                                <CitiesPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/cities/:id" element={
                            <ProtectedRoute>
                                <CityPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/cities/:id/edit" element={
                            <ProtectedRoute>
                                <CityPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/cities/new" element={
                            <ProtectedRoute>
                                <CityPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/scenarios" element={
                            <ProtectedRoute>
                                <ScenariosPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/scenarios/:id" element={
                            <ProtectedRoute>
                                <ScenarioPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/scenarios/:id/edit" element={
                            <ProtectedRoute>
                                <ScenarioPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/scenarios/new" element={
                            <ProtectedRoute>
                                <ScenarioPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/types" element={
                            <ProtectedRoute>
                                <TypesPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/types/:id" element={
                            <ProtectedRoute>
                                <TypePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/types/:id/edit" element={
                            <ProtectedRoute>
                                <TypePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/types/new" element={
                            <ProtectedRoute>
                                <TypePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/table-titles" element={
                            <ProtectedRoute>
                                <TableTitlesPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/table-titles/:id" element={
                            <ProtectedRoute>
                                <TableTitlePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/table-titles/:id/edit" element={
                            <ProtectedRoute>
                                <TableTitlePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/table-titles/new" element={
                            <ProtectedRoute>
                                <TableTitlePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/emergency-services" element={
                            <ProtectedRoute>
                                <EmergencyServicesPage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/emergency-services/:id" element={
                            <ProtectedRoute>
                                <EmergencyServicePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/emergency-services/:id/edit" element={
                            <ProtectedRoute>
                                <EmergencyServicePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/emergency-services/new" element={
                            <ProtectedRoute>
                                <EmergencyServicePage/>
                            </ProtectedRoute>
                        }/>
                        <Route path="/documents" element={
                            <ProtectedRoute>
                                <DocumentsPage/>
                            </ProtectedRoute>
                        }/>
                    </Route>

                    {/* ВСЁ ОСТАЛЬНОЕ — редирект на главную */}
                    <Route path="*" element={<Navigate to="/" replace/>}/>

                </Routes>
            </AuthProvider>
        </BrowserRouter>
    );
}