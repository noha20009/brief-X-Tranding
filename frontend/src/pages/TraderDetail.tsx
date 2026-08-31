import { useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import {
  buyAsset,
  cancelOrder,
  fetchAssets,
  fetchOrders,
  fetchPerformance,
  fetchPortfolio,
  fetchTrader,
  downloadExport,
  placeOrder,
  sellAsset,
} from '../api'
import ErrorAlert from '../components/ErrorAlert'
import Loader from '../components/Loader'
import StatCard from '../components/StatCard'
import { useApi } from '../hooks/useApi'
import type { OrderDirection, OrderType } from '../types'
import { formatCurrency, formatDate, formatPercent } from '../utils/format'
import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'

export default function TraderDetail() {
  const { id } = useParams<{ id: string }>()
  const traderId = Number(id)

  const trader = useApi(() => fetchTrader(traderId), [traderId])
  const portfolio = useApi(() => fetchPortfolio(traderId), [traderId])
  const performance = useApi(() => fetchPerformance(traderId), [traderId])
  const assets = useApi(fetchAssets)
  const orders = useApi(() => fetchOrders(traderId), [traderId])

  const [action, setAction] = useState<'ACHAT' | 'VENTE'>('ACHAT')
  const [assetId, setAssetId] = useState<number>(0)
  const [quantite, setQuantite] = useState('1')
  const [busy, setBusy] = useState(false)
  const [msg, setMsg] = useState<{ ok: boolean; text: string } | null>(null)

  const [orderType, setOrderType] = useState<OrderType>('LIMIT')
  const [orderDir, setOrderDir] = useState<OrderDirection>('BUY')
  const [orderAssetId, setOrderAssetId] = useState<number>(0)
  const [orderQty, setOrderQty] = useState('1')
  const [orderPrice, setOrderPrice] = useState('')
  const [orderBusy, setOrderBusy] = useState(false)
  const [orderMsg, setOrderMsg] = useState<{ ok: boolean; text: string } | null>(null)
  const [cancelling, setCancelling] = useState<number | null>(null)
  const [exportBusy, setExportBusy] = useState(false)
  const [exportMsg, setExportMsg] = useState<{ ok: boolean; text: string } | null>(null)

  if (
    trader.loading ||
    portfolio.loading ||
    performance.loading ||
    assets.loading
  )
    return <Loader />
  if (trader.error) return <ErrorAlert message={trader.error} />
  if (portfolio.error) return <ErrorAlert message={portfolio.error} />
  if (performance.error) return <ErrorAlert message={performance.error} />
  if (assets.error) return <ErrorAlert message={assets.error} />

  const t = trader.data!
  const pf = portfolio.data!
  const perf = performance.data!
  const assetOptions = assets.data!

  const chartData = Object.entries(
    perf.transactions.reduce<Record<string, number>>((acc, tr) => {
      acc[tr.assetNom] = (acc[tr.assetNom] ?? 0) + tr.quantite
      return acc
    }, {})
  ).map(([name, value]) => ({ name, value }))

  const handleTrade = async (e: React.FormEvent) => {
    e.preventDefault()
    setMsg(null)
    if (!assetId) {
      setMsg({ ok: false, text: 'Veuillez choisir un actif.' })
      return
    }
    const qty = parseInt(quantite, 10)
    if (isNaN(qty) || qty <= 0) {
      setMsg({ ok: false, text: 'Quantité invalide.' })
      return
    }
    setBusy(true)
    try {
      if (action === 'ACHAT') {
        await buyAsset({ traderId, assetId, quantite: qty })
      } else {
        await sellAsset({ traderId, assetId, quantite: qty })
      }
      setMsg({
        ok: true,
        text: `${action === 'ACHAT' ? 'Achat' : 'Vente'} exécuté avec succès.`,
      })
      await Promise.all([
        portfolio.reload(),
        performance.reload(),
        trader.reload(),
        orders.reload(),
      ])
    } catch {
      setMsg({ ok: false, text: 'Opération refusée (solde ou quantité insuffisante).' })
    } finally {
      setBusy(false)
    }
  }

  const handlePlaceOrder = async (e: React.FormEvent) => {
    e.preventDefault()
    setOrderMsg(null)
    if (!orderAssetId) {
      setOrderMsg({ ok: false, text: 'Veuillez choisir un actif.' })
      return
    }
    const qty = parseInt(orderQty, 10)
    if (isNaN(qty) || qty <= 0) {
      setOrderMsg({ ok: false, text: 'Quantité invalide.' })
      return
    }
    const price = parseFloat(orderPrice)
    if (isNaN(price) || price <= 0) {
      setOrderMsg({ ok: false, text: 'Prix déclencheur invalide.' })
      return
    }
    if (orderType === 'STOP_LOSS' || orderType === 'TAKE_PROFIT') {
      setOrderDir('SELL')
    }
    setOrderBusy(true)
    try {
      await placeOrder({
        traderId,
        assetId: orderAssetId,
        type: orderType,
        direction:
          orderType === 'STOP_LOSS' || orderType === 'TAKE_PROFIT' ? 'SELL' : orderDir,
        quantite: qty,
        prixDeclaration: price,
      })
      setOrderMsg({ ok: true, text: 'Ordre placé avec succès.' })
      setOrderPrice('')
      await orders.reload()
    } catch {
      setOrderMsg({ ok: false, text: 'Impossible de placer l\'ordre.' })
    } finally {
      setOrderBusy(false)
    }
  }

  const handleCancelOrder = async (orderId: number) => {
    setCancelling(orderId)
    try {
      await cancelOrder(orderId, traderId)
      await orders.reload()
    } catch {
      // ignore
    } finally {
      setCancelling(null)
    }
  }

  const handleExport = async (format: 'csv' | 'excel') => {
    setExportBusy(true)
    setExportMsg(null)
    try {
      await downloadExport(format, traderId)
      setExportMsg({ ok: true, text: 'Export téléchargé.' })
    } catch {
      setExportMsg({ ok: false, text: 'Échec de l’export.' })
    } finally {
      setExportBusy(false)
    }
  }

  const perfUp = perf.performancePourcentage >= 0

  return (
    <div className="page">
      <div className="page-head">
        <div>
          <Link to="/traders" className="back-link">
            <span className="material-symbols-outlined" style={{ fontSize: 18 }}>
              arrow_back
            </span>
            Retour
          </Link>
          <h1 className="page-title">{t.nom}</h1>
          <p className="page-sub">Portefeuille et performance</p>
        </div>
      </div>

      {msg && (
        <div className={msg.ok ? 'banner' : 'banner banner-error'} role="status">
          <span className="material-symbols-outlined icon">
            {msg.ok ? 'check_circle' : 'error'}
          </span>
          <div>
            <p className="banner-title">{msg.ok ? 'Succès' : 'Opération refusée'}</p>
            <p className="banner-sub">{msg.text}</p>
          </div>
        </div>
      )}

      <section className="stats-grid">
        <StatCard
          label="Balance"
          value={formatCurrency(pf.solde)}
          accent="blue"
        />
        <StatCard
          label="Valeur portefeuille"
          value={formatCurrency(pf.valeurPortefeuille)}
          accent="green"
        />
        <StatCard
          label="Valeur totale"
          value={formatCurrency(pf.valeurTotale)}
          accent="purple"
        />
        <StatCard
          label="Performance"
          value={formatPercent(perf.performancePourcentage)}
          accent={perfUp ? 'green' : 'red'}
          sub={`${formatCurrency(perf.gainsPertesRealises)} réalisés`}
        />
      </section>

      <div className="detail-grid">
        <div className="card">
          <h2 className="card-title">Opération Rapide</h2>
          <div className="segmented" role="tablist">
            <button
              type="button"
              className={action === 'ACHAT' ? 'seg-active green' : ''}
              onClick={() => setAction('ACHAT')}
            >
              Acheter
            </button>
            <button
              type="button"
              className={action === 'VENTE' ? 'seg-active red' : ''}
              onClick={() => setAction('VENTE')}
            >
              Vendre
            </button>
          </div>

          <form onSubmit={handleTrade} className="form">
            <label className="field">
              <span>Actif</span>
              <div className="select-wrap">
                <span className="icon material-symbols-outlined">currency_exchange</span>
                <select
                  className="has-icon"
                  value={assetId}
                  onChange={(e) => setAssetId(Number(e.target.value))}
                >
                  <option value={0}>— Choisir un actif —</option>
                  {assetOptions.map((a) => (
                    <option key={a.id} value={a.id}>
                      {a.code} — {a.nom} ({formatCurrency(a.prixUnitaire)})
                    </option>
                  ))}
                </select>
                <span className="caret material-symbols-outlined">arrow_drop_down</span>
              </div>
            </label>
            <label className="field">
              <span>Quantité</span>
              <div className="input-wrap">
                <span className="icon material-symbols-outlined">numbers</span>
                <input
                  type="number"
                  min="1"
                  step="1"
                  value={quantite}
                  onChange={(e) => setQuantite(e.target.value)}
                />
              </div>
            </label>
            <button
              type="submit"
              className={action === 'ACHAT' ? 'btn btn-green' : 'btn btn-danger'}
              disabled={busy}
            >
              {busy
                ? 'Exécution…'
                : action === 'ACHAT'
                  ? 'Confirmer l’achat'
                  : 'Confirmer la vente'}
            </button>
          </form>
          <p className="fee-note">
            <span className="material-symbols-outlined" style={{ fontSize: 15 }}>
              info
            </span>
            Spread (achat/vente) et frais de 0,1&nbsp;% sont appliqués sur chaque opération selon le cours temps réel.
          </p>
        </div>

        <div className="chart-card">
          <h2>Volume par actif</h2>
          {chartData.length === 0 ? (
            <p className="empty">Aucune transaction</p>
          ) : (
            <ResponsiveContainer width="100%" height={220}>
              <BarChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#343535" />
                <XAxis dataKey="name" tick={{ fontSize: 12, fill: '#e3bfb1' }} stroke="#555555" />
                <YAxis allowDecimals={false} tick={{ fontSize: 12, fill: '#e3bfb1' }} stroke="#555555" />
                <Tooltip contentStyle={{ background: '#454545', border: '1px solid #555555', borderRadius: 12 }} />
                <Bar dataKey="value" fill="#ff6000" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>
      </div>

      <div className="grid-2 order-grid">
        <div className="card">
          <h2 className="card-title">
            <span className="material-symbols-outlined" style={{ color: 'var(--primary)' }}>
              swap_vert
            </span>
            Ordre conditionnel
          </h2>
          {orderMsg && (
            <div className={orderMsg.ok ? 'banner' : 'banner banner-error'} role="status">
              <span className="material-symbols-outlined icon">
                {orderMsg.ok ? 'check_circle' : 'error'}
              </span>
              <div>
                <p className="banner-title">{orderMsg.ok ? 'Succès' : 'Ordre refusé'}</p>
                <p className="banner-sub">{orderMsg.text}</p>
              </div>
            </div>
          )}
          <form onSubmit={handlePlaceOrder} className="form">
            <div className="segmented" role="tablist">
              <button
                type="button"
                className={orderType === 'LIMIT' ? 'seg-active' : ''}
                onClick={() => setOrderType('LIMIT')}
              >
                Limite
              </button>
              <button
                type="button"
                className={orderType === 'STOP_LOSS' ? 'seg-active red' : ''}
                onClick={() => {
                  setOrderType('STOP_LOSS')
                  setOrderDir('SELL')
                }}
              >
                Stop-loss
              </button>
              <button
                type="button"
                className={orderType === 'TAKE_PROFIT' ? 'seg-active green' : ''}
                onClick={() => {
                  setOrderType('TAKE_PROFIT')
                  setOrderDir('SELL')
                }}
              >
                Take-profit
              </button>
            </div>

            {orderType === 'LIMIT' && (
              <div className="segmented" role="tablist">
                <button
                  type="button"
                  className={orderDir === 'BUY' ? 'seg-active green' : ''}
                  onClick={() => setOrderDir('BUY')}
                >
                  Acheter
                </button>
                <button
                  type="button"
                  className={orderDir === 'SELL' ? 'seg-active red' : ''}
                  onClick={() => setOrderDir('SELL')}
                >
                  Vendre
                </button>
              </div>
            )}

            <label className="field">
              <span>Actif</span>
              <div className="select-wrap">
                <span className="icon material-symbols-outlined">currency_exchange</span>
                <select
                  className="has-icon"
                  value={orderAssetId}
                  onChange={(e) => setOrderAssetId(Number(e.target.value))}
                >
                  <option value={0}>— Choisir un actif —</option>
                  {assetOptions.map((a) => (
                    <option key={a.id} value={a.id}>
                      {a.code} — {a.nom} ({formatCurrency(a.prixUnitaire)})
                    </option>
                  ))}
                </select>
                <span className="caret material-symbols-outlined">arrow_drop_down</span>
              </div>
            </label>

            <div className="field-row">
              <label className="field">
                <span>Quantité</span>
                <div className="input-wrap">
                  <span className="icon material-symbols-outlined">numbers</span>
                  <input
                    type="number"
                    min="1"
                    step="1"
                    value={orderQty}
                    onChange={(e) => setOrderQty(e.target.value)}
                  />
                </div>
              </label>
              <label className="field">
                <span>{orderType === 'LIMIT' ? 'Prix limite' : 'Prix déclencheur'} ($)</span>
                <div className="input-wrap">
                  <span className="icon material-symbols-outlined">attach_money</span>
                  <input
                    type="number"
                    min="0"
                    step="any"
                    value={orderPrice}
                    onChange={(e) => setOrderPrice(e.target.value)}
                  />
                </div>
              </label>
            </div>

            <button type="submit" className="btn btn-primary" disabled={orderBusy}>
              {orderBusy ? 'Placement…' : 'Placer l\'ordre'}
              <span className="material-symbols-outlined" style={{ fontSize: 18 }}>
                arrow_forward
              </span>
            </button>
          </form>
        </div>

        <div className="table-wrap">
          <div className="table-toolbar">
            <h3>Ordres</h3>
            {orders.data && (
              <span className="order-badge">
                {orders.data.filter((o) => o.statut === 'PENDING').length} en attente
              </span>
            )}
          </div>
          {orders.loading ? (
            <Loader />
          ) : orders.error ? (
            <ErrorAlert message={orders.error} />
          ) : orders.data!.length === 0 ? (
            <p className="empty">Aucun ordre placé</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Direction</th>
                  <th>Actif</th>
                  <th className="right">Qté</th>
                  <th className="right">Déclencheur</th>
                  <th>Statut</th>
                  <th className="right">Action</th>
                </tr>
              </thead>
              <tbody>
                {orders.data!.map((o) => (
                  <tr key={o.id}>
                    <td>
                      <span className="badge-outline w-fit">{o.type.replace('_', '-')}</span>
                    </td>
                    <td>
                      <span className={`order-dir ${o.direction === 'BUY' ? 'dir-buy' : 'dir-sell'}`}>
                        {o.direction === 'BUY' ? 'Achat' : 'Vente'}
                      </span>
                    </td>
                    <td>
                      <span className="strong">{o.assetCode}</span> {o.assetNom}
                    </td>
                    <td className="right">{o.quantite}</td>
                    <td className="right">{formatCurrency(o.prixDeclaration)}</td>
                    <td>
                      <span className={`order-status st-${o.statut.toLowerCase()}`}>
                        {o.statut === 'PENDING' ? 'En attente' : o.statut === 'EXECUTED' ? 'Exécuté' : 'Annulé'}
                      </span>
                    </td>
                    <td className="right">
                      {o.statut === 'PENDING' ? (
                        <button
                          className="btn-icon"
                          type="button"
                          disabled={cancelling === o.id}
                          onClick={() => handleCancelOrder(o.id)}
                          title="Annuler l'ordre"
                        >
                          <span className="material-symbols-outlined">close</span>
                        </button>
                      ) : (
                        <span className="muted">—</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      <div className="table-wrap">
        <div className="table-toolbar">
          <h3>Portefeuille actuel</h3>
        </div>
        {pf.actifs.length === 0 ? (
          <p className="empty">Portefeuille vide</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Actif</th>
                <th>Type</th>
                <th className="right">Prix unitaire</th>
                <th className="right">Quantité</th>
                <th className="right">Valeur</th>
              </tr>
            </thead>
            <tbody>
              {pf.actifs.map((item) => (
                <tr key={item.assetId}>
                  <td>
                    <div className="name-cell">
                      <span className="avatar">{item.code.slice(0, 3)}</span>
                      <span>
                        <span className="strong">{item.code}</span>{' '}
                        {item.nom}
                      </span>
                    </div>
                  </td>
                  <td>
                    <span className="badge">{item.type}</span>
                  </td>
                  <td className="right">{formatCurrency(item.prixUnitaire)}</td>
                  <td className="right">{item.quantite}</td>
                  <td className="right strong">{formatCurrency(item.valeur)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div className="table-wrap">
        <div className="table-toolbar">
          <h3>Historique des transactions</h3>
          <div className="export-actions">
            {exportMsg && (
              <span className={`export-note ${exportMsg.ok ? 'ok' : 'err'}`}>
                {exportMsg.text}
              </span>
            )}
            <button
              type="button"
              className="btn btn-ghost"
              disabled={exportBusy}
              onClick={() => handleExport('csv')}
            >
              <span className="material-symbols-outlined" style={{ fontSize: 18 }}>
                download
              </span>
              {exportBusy ? 'Export…' : 'CSV'}
            </button>
            <button
              type="button"
              className="btn btn-ghost"
              disabled={exportBusy}
              onClick={() => handleExport('excel')}
            >
              <span className="material-symbols-outlined" style={{ fontSize: 18 }}>
                table_chart
              </span>
              Excel
            </button>
          </div>
        </div>
        <table className="table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Type</th>
              <th>Actif</th>
              <th className="right">Quantité</th>
              <th className="right">Prix</th>
              <th className="right">Frais</th>
              <th className="right">Total</th>
            </tr>
          </thead>
          <tbody>
            {perf.transactions.map((tr) => (
              <tr key={tr.id}>
                <td>{formatDate(tr.date)}</td>
                <td>
                  <span className={`type-pill ${tr.type === 'ACHAT' ? 'buy' : 'sell'}`}>
                    {tr.type === 'ACHAT' ? 'Achat' : 'Vente'}
                  </span>
                </td>
                <td>
                  <span className="strong">{tr.assetCode}</span>{' '}
                  {tr.assetNom}
                </td>
                <td className="right">{tr.quantite}</td>
                <td className="right">{formatCurrency(tr.prixUnitaire)}</td>
                <td className="right muted">{tr.frais > 0 ? formatCurrency(tr.frais) : '—'}</td>
                <td className="right strong">{formatCurrency(tr.montantTotal)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}