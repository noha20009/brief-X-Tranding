import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { clearSession, getSession } from '../utils/auth'
import logo from '../photo/xtrade_logo.png'

const navItems = [
  { to: '/dashboard', label: 'Tableau de bord', end: true },
  { to: '/traders', label: 'Traders' },
  { to: '/actifs', label: 'Actifs' },
]

export default function Layout() {
  const navigate = useNavigate()
  const session = getSession()

  const handleLogout = () => {
    clearSession()
    navigate('/', { replace: true })
  }

  return (
    <div className="layout">
      <header className="topbar">
        <div className="topbar-inner">
          <NavLink to="/dashboard" className="brand">
            <span className="brand-logo">
              <img src={logo} alt="XTrade" />
            </span>
            <span className="brand-name">
              X<span>Trade</span>
            </span>
          </NavLink>
          <nav className="nav">
            {navItems.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                end={item.end}
                className={({ isActive }) =>
                  isActive ? 'nav-link active' : 'nav-link'
                }
              >
                {item.label}
              </NavLink>
            ))}
          </nav>
          <div className="nav-user">
            <span className="nav-avatar">{session?.nom?.charAt(0) ?? '?'}</span>
            <button className="btn-icon" type="button" onClick={handleLogout} title="Se déconnecter">
              <span className="material-symbols-outlined">logout</span>
            </button>
          </div>
        </div>
      </header>
      <main className="main">
        <Outlet />
      </main>
      <footer className="footer">
        XTrade &copy; {new Date().getFullYear()} — Plateforme de trading
        d&apos;entra&icirc;nement
      </footer>
    </div>
  )
}