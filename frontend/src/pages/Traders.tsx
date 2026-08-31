import { useState } from 'react'
import { Link } from 'react-router-dom'
import { createTrader, fetchTraders } from '../api'
import ErrorAlert from '../components/ErrorAlert'
import Loader from '../components/Loader'
import { useApi } from '../hooks/useApi'
import { formatCurrency, initials } from '../utils/format'

export default function Traders() {
  const { data, loading, error, reload } = useApi(fetchTraders)
  const [nom, setNom] = useState('')
  const [balance, setBalance] = useState('10000')
  const [submitting, setSubmitting] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setFormError(null)
    setSuccess(null)
    if (!nom.trim()) {
      setFormError('Le nom est obligatoire.')
      return
    }
    const bal = parseFloat(balance)
    if (isNaN(bal) || bal <= 0) {
      setFormError('La balance doit être un nombre positif.')
      return
    }
    setSubmitting(true)
    try {
      await createTrader({ nom: nom.trim(), balance: bal })
      setNom('')
      setSuccess(`Le compte de trading de ${nom.trim()} a été initialisé.`)
      await reload()
    } catch {
      setFormError('Impossible de créer le trader. Vérifiez la connexion.')
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return <Loader />
  if (error) return <ErrorAlert message={error} />

  return (
    <div className="page">
      {success && (
        <div className="banner" role="status">
          <span className="material-symbols-outlined icon">check_circle</span>
          <div>
            <p className="banner-title">Trader créé avec succès</p>
            <p className="banner-sub">{success}</p>
          </div>
        </div>
      )}

      <div className="page-head">
        <div>
          <h1 className="page-title">Gestion des Traders</h1>
          <p className="page-sub">Surveillez et gérez les comptes de trading actifs.</p>
        </div>
      </div>

      <div className="grid-2">
        <div className="table-wrap">
          <div className="table-toolbar">
            <h3>Traders Actifs</h3>
            <span className="material-symbols-outlined" style={{ fontSize: 16 }}>
              filter_list
            </span>
          </div>
          <div className="table">
            <table>
              <thead>
                <tr>
                  <th>Nom</th>
                  <th className="right">Balance</th>
                  <th className="right">Portefeuille</th>
                  <th className="right">Total</th>
                </tr>
              </thead>
              <tbody>
                {data!.map((t) => (
                  <tr key={t.id}>
                    <td>
                      <div className="name-cell">
                        <span className="avatar">{initials(t.nom)}</span>
                        <Link className="link" to={`/traders/${t.id}`}>
                          {t.nom}
                        </Link>
                      </div>
                    </td>
                    <td className="right">{formatCurrency(t.balance)}</td>
                    <td className="right">
                      <span className="dot" style={{ background: t.valeurPortefeuille > 0 ? 'var(--green)' : 'var(--red)' }} />
                      {formatCurrency(t.valeurPortefeuille)}
                    </td>
                    <td className="right strong">{formatCurrency(t.valeurTotale)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="card">
          <h2 className="card-title">
            <span className="material-symbols-outlined" style={{ color: 'var(--primary)' }}>
              person_add
            </span>
            Nouveau Trader
          </h2>
          {formError && <ErrorAlert message={formError} />}
          <form onSubmit={handleSubmit} className="form">
            <label className="field">
              <span>Nom complet</span>
              <div className="input-wrap">
                <span className="icon material-symbols-outlined">person</span>
                <input
                  type="text"
                  value={nom}
                  onChange={(e) => setNom(e.target.value)}
                  placeholder="Ex : Nouha Mach"
                />
              </div>
            </label>
            <label className="field">
              <span>Balance initiale (USD)</span>
              <div className="input-wrap">
                <span className="icon material-symbols-outlined">attach_money</span>
                <input
                  type="number"
                  min="0"
                  step="any"
                  value={balance}
                  onChange={(e) => setBalance(e.target.value)}
                />
              </div>
            </label>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Création…' : 'Créer le trader'}
              <span className="material-symbols-outlined" style={{ fontSize: 18 }}>
                arrow_forward
              </span>
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}