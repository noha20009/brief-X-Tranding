import { Link } from 'react-router-dom'
import { fetchStats, fetchTraders } from '../api'
import ErrorAlert from '../components/ErrorAlert'
import Loader from '../components/Loader'
import StatCard from '../components/StatCard'
import { useApi } from '../hooks/useApi'
import { formatCompact, formatCurrency, initials } from '../utils/format'
import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts'

const DONUT_COLORS = ['#FF6000', '#FFA559', '#FFE6C7', '#ffb77e', '#e3bfb1', '#aa8a7d']

export default function Dashboard() {
  const stats = useApi(fetchStats)
  const traders = useApi(fetchTraders)

  if (stats.loading || traders.loading) return <Loader />
  if (stats.error) return <ErrorAlert message={stats.error} />
  if (traders.error) return <ErrorAlert message={traders.error} />

  const s = stats.data!
  const volumeData = Object.entries(s.volumeParActif)
    .map(([name, value]) => ({ name, value }))
    .sort((a, b) => b.value - a.value)
  const total = volumeData.reduce((acc, d) => acc + d.value, 0)

  return (
    <div className="page">
      <div className="page-head">
        <div>
          <h1 className="page-title">Tableau de bord</h1>
          <p className="page-sub">Vue d&apos;ensemble de la simulation de march&eacute;</p>
        </div>
      </div>

      <section className="stats-grid">
        <StatCard
          label="Traders"
          value={String(s.tradersCount)}
          icon="group"
          accent="purple"
          sub={`${s.transactionsCount} opérations récentes`}
        />
        <StatCard
          label="Actifs"
          value={String(s.assetsCount)}
          icon="account_balance"
          accent="blue"
          sub="Actions et cryptos"
        />
        <StatCard
          label="Transactions"
          value={String(s.transactionsCount)}
          icon="sync"
          accent="green"
          sub="Aujourd&apos;hui"
        />
        <StatCard
          label="Volume total"
          value={formatCurrency(s.totalAchats + s.totalVentes)}
          icon="account_balance_wallet"
          accent="red"
          sub={`${formatCompact(s.totalAchats + s.totalVentes)} cumulés`}
        />
      </section>

      <div className="grid-2">
        <div className="chart-card">
          <h2>Volume par actif</h2>
          {volumeData.length === 0 ? (
            <p className="empty">Aucune donnée</p>
          ) : (
            <>
              <ResponsiveContainer width="100%" height={280}>
                <PieChart>
                  <Pie
                    data={volumeData}
                    dataKey="value"
                    nameKey="name"
                    cx="50%"
                    cy="50%"
                    innerRadius={70}
                    outerRadius={110}
                    paddingAngle={2}
                    stroke="none"
                  >
                    {volumeData.map((_, i) => (
                      <Cell key={i} fill={DONUT_COLORS[i % DONUT_COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip formatter={(v) => formatCompact(Number(v ?? 0))} />
                </PieChart>
              </ResponsiveContainer>
              <div className="chart-legend">
                {volumeData.map((d, i) => (
                  <div key={d.name} className="legend-item">
                    <span
                      className="legend-dot"
                      style={{ background: DONUT_COLORS[i % DONUT_COLORS.length] }}
                    />
                    {d.name} {total > 0 ? Math.round((d.value / total) * 100) : 0}%
                  </div>
                ))}
              </div>
            </>
          )}
        </div>

        <div className="table-wrap">
          <div className="table-toolbar">
            <h3>Résumé Traders</h3>
          </div>
          <table className="table">
            <thead>
              <tr>
                <th>Nom</th>
                <th className="right">Portefeuille</th>
                <th className="right">Balance</th>
              </tr>
            </thead>
            <tbody>
              {traders.data!.slice(0, 6).map((t) => (
                <tr key={t.id}>
                  <td>
                    <div className="name-cell">
                      <span className="avatar">{initials(t.nom)}</span>
                      <Link className="link" to={`/traders/${t.id}`}>
                        {t.nom}
                      </Link>
                    </div>
                  </td>
                  <td className="right">{formatCurrency(t.valeurPortefeuille)}</td>
                  <td className="right strong">{formatCurrency(t.balance)}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <Link className="view-all" to="/traders">
            Voir tout
          </Link>
        </div>
      </div>

      <section className="grid-2">
        <div className="marche-card">
          <div>
            <div className="m-label">Total Achats</div>
            <div className="m-value">{formatCurrency(s.totalAchats)}</div>
          </div>
          <div className="m-pill">
            <span className="material-symbols-outlined">trending_up</span>
          </div>
        </div>
        <div className="marche-card sell">
          <div>
            <div className="m-label">Total Ventes</div>
            <div className="m-value">{formatCurrency(s.totalVentes)}</div>
          </div>
          <div className="m-pill">
            <span className="material-symbols-outlined">trending_down</span>
          </div>
        </div>
      </section>
    </div>
  )
}