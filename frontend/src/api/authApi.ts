import api from "./api";

export async function login(login: string, password: string) {

    const form = new URLSearchParams();

    form.append("login", login);
    form.append("password", password);

    await api.post(
        "/login",
        form,
        {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }
    );
}

export async function me() {
    const response = await api.get("/api/auth/me");
    return response.data;
}