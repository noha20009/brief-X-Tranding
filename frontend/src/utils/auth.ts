export interface AuthSession {
  token: string
  traderId: number
  nom: string
  email: string
  role: string
}

const KEY = 'xtrade_session'

export function getSession(): AuthSession | null {
  try {
    const raw = localStorage.getItem(KEY)
    return raw ? (JSON.parse(raw) as AuthSession) : null
  } catch {
    return null
  }
}

export function setSession(session: AuthSession): void {
  localStorage.setItem(KEY, JSON.stringify(session))
}

export function clearSession(): void {
  localStorage.removeItem(KEY)
}