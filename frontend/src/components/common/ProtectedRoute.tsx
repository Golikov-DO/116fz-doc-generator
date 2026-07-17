import { useEffect, useState } from "react"
import { Navigate } from "react-router-dom"
import * as React from "react";

export default function ProtectedRoute({ children }: { children: React.ReactNode }) {
    const [auth, setAuth] = useState<boolean | null>(null)

    useEffect(() => {
        fetch('/api/auth/me', { credentials: 'include' })
            .then(async res => {
                if (!res.ok) {
                    setAuth(false)
                    return
                }
                const text = await res.text()
                // Если тело пустое или "null" — не залогинен
                setAuth(text.length > 0 && text !== "null")
            })
            .catch(() => setAuth(false))
    }, [])

    if (auth === null) return <div>Загрузка...</div>
    if (!auth) return <Navigate to="/" replace />

    return <>{children}</>
}