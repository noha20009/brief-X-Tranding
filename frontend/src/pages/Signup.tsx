import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { getErrorMessage, register } from '../api'
import { setSession } from '../utils/auth'
import logo from '../photo/xtrade_logo.png'

export default function Signup() {
  const navigate = useNavigate()
  const [nom, setNom] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [balance, setBalance] = useState(10000)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    if (!nom.trim() || !email.trim() || !password.trim()) {
      setError('Tous les champs sont obligatoires (nom, email, mot de passe).')
      return
    }
    setBusy(true)
    try {
      const res = await register({ nom: nom.trim(), email: email.trim(), password, balance })
      setSession({
        token: res.token,
        traderId: res.traderId,
        nom: res.nom,
        email: res.email,
        role: res.role,
      })
      navigate('/dashboard')
    } catch (err) {
      setError(getErrorMessage(err, 'Impossible de créer le compte. Vérifiez le serveur.'))
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
          <p>Rejoignez l&apos;environnement de trading haute performance.</p>
        </div>

        {error && <div className="error-alert">{error}</div>}

        <form className="form" onSubmit={handleSubmit}>
          <div>
            <label className="auth-label" htmlFor="name">
              Nom complet
            </label>
            <div className="auth-input-wrap">
              <span className="icon material-symbols-outlined">person</span>
              <input
                className="auth-input"
                id="name"
                type="text"
                placeholder="Nouha Ma"
                value={nom}
                onChange={(e) => setNom(e.target.value)}
              />
            </div>
          </div>

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
                placeholder="Nouha@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
          </div>

          <div>
            <label className="auth-label" htmlFor="password">
              Mot de passe
            </label>
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

          <div>
            <span className="auth-label" style={{ display: 'block', marginBottom: 8 }}>
              Solde virtuel initial
            </span>
            <div className="balance-options">
              <div style={{ flex: 1, position: 'relative' }}>
                <input
                  type="radio"
                  id="bal_10k"
                  name="balance"
                  value={10000}
                  checked={balance === 10000}
                  onChange={() => setBalance(10000)}
                />
                <label htmlFor="bal_10k">10,000 $ USD</label>
              </div>
              <div style={{ flex: 1, position: 'relative' }}>
                <input
                  type="radio"
                  id="bal_50k"
                  name="balance"
                  value={50000}
                  checked={balance === 50000}
                  onChange={() => setBalance(50000)}
                />
                <label htmlFor="bal_50k">50,000 $ USD</label>
              </div>
            </div>
          </div>

          <button className="btn-auth" type="submit" disabled={busy}>
            {busy ? 'Création…' : 'Créer un compte'}
            <span className="material-symbols-outlined" style={{ fontSize: 20 }}>
              arrow_forward
            </span>
          </button>
        </form>

        <p className="auth-switch">
          Déjà un compte ? <Link to="/login">Connectez-vous</Link>
        </p>
      </main>
    </div>
  )
}