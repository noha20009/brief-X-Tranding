import { useMemo, useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { getErrorMessage, login } from '../api'
import { setSession } from '../utils/auth'
import logo from '../photo/xtrade_logo.png'

export default function Login() {
  const navigate = useNavigate()
  const location = useLocation()
  const from = useMemo(
    () => (location.state as { from?: { pathname?: string } } | null)?.from?.pathname,
    [location.state],
  )
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    if (!email.trim() || !password.trim()) {
      setError('Veuillez renseigner votre email et votre mot de passe.')
      return
    }
    setBusy(true)
    try {
      const res = await login({ email: email.trim(), password })
      setSession({
        token: res.token,
        traderId: res.traderId,
        nom: res.nom,
        email: res.email,
        role: res.role,
      })
      navigate(from ?? '/dashboard', { replace: true })
    } catch (err) {
      setError(getErrorMessage(err, 'Impossible de se connecter. Vérifiez le serveur.'))
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-glow g1" />
      <div className="auth-glow g2" />

      <main className="auth-card">
        <div className="auth-brand">
          <Link to="/" className="logo-row" style={{ textDecoration: 'none' }}>
            <img className="brand-img" src={logo} alt="XTrade" />
            <h1>XTrade</h1>
          </Link>
          <p>Accédez à votre espace de trading</p>
        </div>

        {error && <div className="error-alert">{error}</div>}

        <form className="form" onSubmit={handleSubmit}>
          <div>
            <label className="auth-label" htmlFor="email">
              Email
            </label>
            <div className="auth-input-wrap">
              <span className="icon material-symbols-outlined">mail</span>
              <input
                className="auth-input"
                id="email"
                type="email"
                placeholder="nom@gmail.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
          </div>

          <div>
            <div className="auth-actions">
              <label className="auth-label" htmlFor="password">
                Mot de passe
              </label>
              <a className="forgot-link" href="#">
                Oublié ?
              </a>
            </div>
            <div className="auth-input-wrap">
              <span className="icon material-symbols-outlined">lock</span>
              <input
                className="auth-input"
                id="password"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <label className="field" style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
            <input type="checkbox" style={{ width: 16, height: 16 }} />
            <span style={{ textTransform: 'none', letterSpacing: 0, opacity: 1 }}>
              Se souvenir de moi
            </span>
          </label>

          <button className="btn-auth" type="submit" disabled={busy}>
            {busy ? 'Connexion…' : 'Se connecter'}
            <span className="material-symbols-outlined" style={{ fontSize: 20 }}>
              arrow_forward
            </span>
          </button>
        </form>

        <p className="auth-switch">
          Pas de compte ? <Link to="/signup">Inscrivez-vous</Link>
        </p>
      </main>
    </div>
  )
}