import { useEffect, useState } from "react"
import { useParams, useLocation, useNavigate } from "react-router-dom"
import {
    getOrganization,
    updateOrganization,
    type Organization,
} from "../../api/organizationDetailsApi"

export default function OrganizationForm() {
    const { id } = useParams()
    const navigate = useNavigate()
    const location = useLocation()

    const [organization, setOrganization] = useState<Organization>()
    const [form, setForm] = useState<Organization>()
    const [editMode, setEditMode] = useState(location.state?.editMode ?? false)

    const hasChanges = JSON.stringify(form) !== JSON.stringify(organization)

    const updateField = <K extends keyof Organization>(field: K, value: Organization[K]) => {
        setForm((prev) => (prev ? { ...prev, [field]: value } : prev))
    }

    const handleSave = async () => {
        if (!form) return
        try {
            const updated = await updateOrganization(form)
            setOrganization(updated)
            setForm(structuredClone(updated))
        } catch (e) {
            console.error(e)
        }
    }

    useEffect(() => {
        if (!id) return
        getOrganization(Number(id))
            .then((org) => {
                setOrganization(org)
                setForm(structuredClone(org))
            })
            .catch(console.error)
    }, [id])

    return (
        <div
            style={{
                background: "#fff",
                border: "1px solid #ddd",
                borderTop: "3px solid #4caf50",
                borderRadius: "4px",
                padding: "24px",
                maxWidth: "700px",
            }}
        >
            <h2 style={{ margin: "0 0 24px", fontSize: "22px", fontWeight: 700, color: "#1a1a1a" }}>
                Организация
            </h2>

            <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
                <Field label="Полное наименование">
                    <input
                        style={input}
                        disabled={!editMode}
                        value={form?.organizationName ?? ""}
                        onChange={(e) => updateField("organizationName", e.target.value)}
                    />
                </Field>

                <Field label="Краткое наименование">
                    <input
                        style={input}
                        disabled={!editMode}
                        value={form?.organizationShortName ?? ""}
                        onChange={(e) => updateField("organizationShortName", e.target.value)}
                    />
                </Field>

                <Field label="Вид деятельности">
          <textarea
              style={{ ...input, minHeight: "80px", resize: "vertical" }}
              disabled={!editMode}
              value={form?.organizationTypeActivity ?? ""}
              onChange={(e) => updateField("organizationTypeActivity", e.target.value)}
          />
                </Field>

                <label style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "15px" }}>
                    <input
                        type="checkbox"
                        disabled={!editMode}
                        checked={form?.oneTerritory ?? false}
                        onChange={(e) => updateField("oneTerritory", e.target.checked)}
                    />
                    ОПО на одной территории
                </label>
            </div>

            <div style={{ marginTop: "24px", display: "flex", gap: "12px" }}>
                {!editMode ? (
                    <>
                        <button style={primaryBtn} onClick={() => setEditMode(true)}>
                            Редактировать
                        </button>
                        <button style={outlineBtn} onClick={() => navigate("/organizations")}>
                            Назад к списку
                        </button>
                    </>
                ) : hasChanges ? (
                    <>
                        <button style={primaryBtn} onClick={handleSave}>
                            Сохранить
                        </button>
                        <button style={outlineBtn} onClick={() => setForm(structuredClone(organization!))}>
                            Отмена
                        </button>
                    </>
                ) : (
                    <button style={outlineBtn} onClick={() => navigate("/organizations")}>
                        Назад к списку
                    </button>
                )}
            </div>
        </div>
    )
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
    return (
        <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
            <span style={{ fontSize: "14px", color: "#374151" }}>{label}</span>
            {children}
        </div>
    )
}

const input: React.CSSProperties = {
    padding: "10px 12px",
    border: "1px solid #ccc",
    borderRadius: "4px",
    fontSize: "15px",
    width: "100%",
    boxSizing: "border-box",
}
const primaryBtn: React.CSSProperties = {
    padding: "10px 20px",
    background: "#4caf50",
    color: "#fff",
    border: "none",
    borderRadius: "4px",
    fontSize: "15px",
    cursor: "pointer",
}
const outlineBtn: React.CSSProperties = {
    padding: "10px 20px",
    background: "#fff",
    color: "#3b5bdb",
    border: "1px solid #3b5bdb",
    borderRadius: "4px",
    fontSize: "15px",
    cursor: "pointer",
}