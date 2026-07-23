import { createContext, useCallback, useEffect, useState } from "react";
import type { ReactNode } from "react";
import * as authApi from "../api/authApi";

export interface User {
    id: number;
    login: string;
    email: string;
    role: "ADMIN" | "USER";
}

interface AuthContextType {
    user: User | null;
    loading: boolean;
    isAuth: boolean;
    isAdmin: boolean;

    refreshUser: () => Promise<void>;
    logout: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextType>({
    user: null,
    loading: true,
    isAuth: false,
    isAdmin: false,
    refreshUser: async () => {},
    logout: async () => {},
});

export function AuthProvider({ children }: { children: ReactNode }) {

    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    const refreshUser = useCallback(async () => {
        try {
            const data = await authApi.me();

            if (data) {
                setUser(data);
            } else {
                setUser(null);
            }
        } catch {
            setUser(null);
        }
    }, []);

    const logout = useCallback(async () => {
        try {
            await authApi.logout();
        } finally {
            setUser(null);
        }
    }, []);

    useEffect(() => {
        refreshUser()
            .finally(() => setLoading(false));
    }, [refreshUser]);

    return (
        <AuthContext.Provider
            value={{
                user,
                loading,
                isAuth: !!user,
                isAdmin: user?.role === "ADMIN",
                refreshUser,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}