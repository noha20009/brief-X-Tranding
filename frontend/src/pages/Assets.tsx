import { useState } from 'react'
import { createAsset, fetchAssets, getErrorMessage, updateAssetPrice } from '../api'
import ErrorAlert from '../components/ErrorAlert'
import Loader from '../components/Loader'
import { useApi } from '../hooks/useApi'
import { useQuotes } from '../hooks/useQuotes'
import type { Asset } from '../types'
import { formatCurrency, formatPercent } from '../utils/format'

export default function Assets() {
  const { data, loading, error, reload } = useApi(fetchAssets)
  const quotes = useQuotes()

  const [formOpen, setFormOpen] = useState(false)
  const [code, setCode] = useState('')
  const [nom, setNom] = useState('')
  const [type, setType] = useState<'STOCK' | 'CRYPTO'>('STOCK')
  const [prixUnitaire, setPrixUnitaire] = useState('100')
  const [submitting, setSubmitting] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)

  const [priceFor, setPriceFor] = useState<Asset | null>(null)
  const [priceValue, setPriceValue] = useState('')
  const [priceBusy, setPriceBusy] = useState(false)
  const [priceError, setPriceError] = useState<string | null>(null)

  if (loading) return <Loader />
  if (error) return <ErrorAlert message={error} />

  const total = data!.length
  const stocks = data!.filter((a) => a.type === 'STOCK').length
  const cryptos = data!.filter((a) => a.type === 'CRYPTO').length

  const livePrice = (a: Asset) => quotes.get(a.id)?.prix ?? a.prixUnitaire
  const liveVariation = (a: Asset) => quotes.get(a.id)?.variationPct ?? 0

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    setFormError(null)
    if (!code.trim() || !nom.trim()) {
      setFormError('Le code et le nom sont obligatoires.')
      return
    }
    const price = parseFloat(prixUnitaire)
    if (isNaN(price) || price <= 0) {
      setFormError('Le prix doit être un nombre positif.')
      return
    }
    setSubmitting(true)
    try {
      await createAsset({ code: code.trim(), nom: nom.trim(), prixUnitaire: price, type })
      setCode('')
      setNom('')
      setFormOpen(false)
      await reload()
    } catch (err) {
      setFormError(getErrorMessage(err, 'Impossible de créer l’actif.'))
    } finally {
      setSubmitting(false)
    }
  }

  const handlePrice = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!priceFor) return
    setPriceError(null)
    const price = parseFloat(priceValue)
    if (isNaN(price) || price <= 0) {
      setPriceError('Prix invalide.')
      return
    }
    setPriceBusy(true)
    try {
      await updateAssetPrice(priceFor.id, price)
      setPriceFor(null)
      setPriceValue('')
      await reload()
    } catch {
      setPriceError('Impossible de mettre à jour le prix.')
    } finally {
      setPriceBusy(false)
    }
  }

  const openPrice = (a: Asset) => {
    setPriceFor(a)
    setPriceValue(String(livePrice(a)))
    setPriceError(null)
  }

  return (
    <div className="page">
      <div className="page-head">
        <div>
          <h1 className="page-title">Actifs financiers</h1>
          <p className="page-sub">Actions et crypto-monnaies — gérez votre catalogue</p>
        </div>
        <button className="btn btn-primary" onClick={() => setFormOpen((v) => !v)}>
          <span className="material-symbols-outlined" style={{ fontSize: 18 }}>
            add
          </span>
          Nouvel actif
        </button>
      </div>

      <section className="bento-stats">
        <div className="bento-stat bento-wide">
          <div className="b-top">
            <span className="b-label">
              <span className="icon material-symbols-outlined">folder_open</span>
              Total actifs
            </span>
            <span className="b-icon material-symbols-outlined">dataset</span>
          </div>
          <div className="b-value">{total}</div>
        </div>
        <div className="bento-stat">
          <div className="b-top">
            <span className="b-label">
              <span className="icon material-symbols-outlined">show_chart</span>
              Actions
            </span>
          </div>
          <div className="b-value">{stocks}</div>
          <span className="b-deco material-symbols-outlined">timeline</span>
        </div>
        <div className="bento-stat">
          <div className="b-top">
            <span className="b-label">
              <span className="icon material-symbols-outlined">currency_bitcoin</span>
              Crypto
            </span>
          </div>
          <div className="b-value">{cryptos}</div>
          <span className="b-deco material-symbols-outlined">toll</span>
        </div>
      </section>

      {formOpen && (
        <section className="card">
          <h2 className="card-title">Ajouter un actif</h2>
          {formError && <ErrorAlert message={formError} />}
          <form onSubmit={handleCreate} className="form">
            <div className="field-row">
              <label className="field">
                <span>Code</span>
                <div className="input-wrap">
                  <span className="icon material-symbols-outlined">tag</span>
                  <input
                    type="text"
                    value={code}
                    onChange={(e) => setCode(e.target.value.toUpperCase())}
                    placeholder="AAPL"
                  />
                </div>
              </label>
              <label className="field">
                <span>Type</span>
                <div className="select-wrap">
                  <select
                    value={type}
                    onChange={(e) => setType(e.target.value as 'STOCK' | 'CRYPTO')}
                  >
                    <option value="STOCK">Action</option>
                    <option value="CRYPTO">Crypto</option>
                  </select>
                  <span className="caret material-symbols-outlined">arrow_drop_down</span>
                </div>
              </label>
            </div>
            <label className="field">
              <span>Nom</span>
              <div className="input-wrap">
                <span className="icon material-symbols-outlined">corporate_fare</span>
                <input
                  type="text"
                  value={nom}
                  onChange={(e) => setNom(e.target.value)}
                  placeholder="Apple Inc."
                />
              </div>
            </label>
            <label className="field">
              <span>Prix unitaire ($)</span>
              <div className="input-wrap">
                <span className="icon material-symbols-outlined">attach_money</span>
                <input
                  type="number"
                  min="0"
                  step="any"
                  value={prixUnitaire}
                  onChange={(e) => setPrixUnitaire(e.target.value)}
                />
              </div>
            </label>
            <div className="modal-actions">
              <button
                type="button"
                className="btn"
                onClick={() => {
                  setFormOpen(false)
                  setFormError(null)
                }}
              >
                Annuler
              </button>
              <button type="submit" className="btn btn-primary" disabled={submitting}>
                {submitting ? 'Création…' : 'Créer l’actif'}
              </button>
            </div>
          </form>
        </section>
      )}

      <section>
        <h2 className="card-title" style={{ marginBottom: 12 }}>
          Catalogue
        </h2>
        <div className="catalogue">
          {data!.map((a) => (
            <div key={a.id} className="catalogue-row">
              <div className="catalogue-main">
                <span className="code-tag">{a.code.slice(0, 3)}</span>
                <div className="catalogue-body">
                  <span className="name">{a.nom}</span>
                  <span className="badge-outline w-fit">{a.type}</span>
                </div>
              </div>
              <div className="catalogue-side">
                <div className="quote-price">
                  <span className="catalogue-price">{formatCurrency(livePrice(a))}</span>
                  <span className={`quote-var ${liveVariation(a) >= 0 ? 'up' : 'down'}`}>
                    {formatPercent(liveVariation(a))}
                  </span>
                </div>
                <button className="btn-icon" onClick={() => openPrice(a)}>
                  <span className="material-symbols-outlined">edit</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      </section>

      {priceFor && (
        <div className="modal-backdrop" onClick={() => setPriceFor(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-head">
              <h2 className="card-title" style={{ marginBottom: 0 }}>
                Modifier le prix
              </h2>
              <button className="btn-icon" onClick={() => setPriceFor(null)}>
                <span className="material-symbols-outlined">close</span>
              </button>
            </div>
            <div className="modal-asset">
              <span className="code-tag">{priceFor.code.slice(0, 3)}</span>
              <div>
                <div className="asset-name">{priceFor.nom}</div>
                <div className="asset-price">
                  Prix actuel : {formatCurrency(livePrice(priceFor))}
                </div>
              </div>
            </div>
            {priceError && <ErrorAlert message={priceError} />}
            <form onSubmit={handlePrice} className="form">
              <label className="field">
                <span>Nouveau prix ($)</span>
                <div className="input-wrap">
                  <span className="icon material-symbols-outlined">attach_money</span>
                  <input
                    type="number"
                    min="0"
                    step="any"
                    value={priceValue}
                    onChange={(e) => setPriceValue(e.target.value)}
                    autoFocus
                  />
                </div>
              </label>
              <div className="modal-actions">
                <button
                  type="button"
                  className="btn btn-outline"
                  onClick={() => setPriceFor(null)}
                >
                  Annuler
                </button>
                <button type="submit" className="btn btn-primary" disabled={priceBusy}>
                  {priceBusy ? 'Enregistrement…' : 'Enregistrer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}