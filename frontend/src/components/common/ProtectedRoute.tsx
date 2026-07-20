import { Navigate } from "react-router-dom";
import * as React from "react";
import { useAuth } from "../../auth/useAuth";

export default function ProtectedRoute({ children }: { children: React.ReactNode }) {
    const { user, loading } = useAuth();

    if (loading) return <div>Загрузка...</div>;
    if (!user) return <Navigate to="/" replace />;

    return <>{children}</>;
}