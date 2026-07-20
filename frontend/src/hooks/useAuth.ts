import { useEffect, useState } from "react";

export interface User {
    id: number;
    username: string;
    email: string;
    role: 'ADMIN' | 'USER';
}

export function useAuth() {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(async res => {
                if (!res.ok) {
                    setUser(null);
                    return;
                }
                const data = await res.json();
                if (data) {
                    setUser({
                        id: data.id,
                        username: data.username,
                        email: data.email,
                        role: data.role
                    });
                }
            })
            .catch(() => setUser(null))
            .finally(() => setLoading(false));
    }, []);

    return { user, loading, isAuth: !!user, isAdmin: user?.role === 'ADMIN' };
}