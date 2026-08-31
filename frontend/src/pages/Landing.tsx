import { Link } from 'react-router-dom'
import logo from '../photo/xtrade_logo.png'
import hero from '../photo/hero.jpg'

export default function Landing() {
  return (
    <div className="landing">
      <header className="land-header">
        <div className="land-header-inner">
          <Link to="/" className="land-brand">
            <img className="brand-img" src={logo} alt="XTrade" />
            <span>XTrade</span>
          </Link>
          <nav className="land-nav">
            <a href="#hero">Accueil</a>
            <a href="#marches">Marchés</a>
            <a href="#fonctionnalites">Fonctionnalités</a>
          </nav>
          <Link to="/signup" className="land-btn">
            Commencer
          </Link>
        </div>
      </header>

      <main className="land-main">
        <section id="hero" className="land-hero">
          <div className="land-hero-left">
            <span className="hero-badge">Environnement de simulation V2.0</span>
            <h1>
              Maîtrisez le Marché <em>sans Risque</em>
            </h1>
            <p>
              La plateforme d&apos;entraînement au trading haute fidélité.
              Plongez dans des conditions de marché réelles, testez vos
              stratégies et construisez votre portefeuille sans risquer un seul
              centime.
            </p>
            <div className="hero-actions">
              <Link to="/signup" className="land-btn hero-glow">
                Commencer gratuitement
              </Link>
              <Link to="/login" className="land-btn outline">
                Se connecter
              </Link>
            </div>
          </div>

          <div className="land-hero-right">
            <div className="hero-card">
              <div className="hero-card-top">
                <div>
                  <p className="hero-balance-label">Solde Virtuel</p>
                  <p className="hero-balance-value">100,000.00 $</p>
                </div>
                <span className="hero-trend">
                  <span className="material-symbols-outlined" style={{ fontSize: 16 }}>
                    trending_up
                  </span>
                  +2.4%
                </span>
              </div>
              <div className="hero-chart">
                <img src={hero} alt="Visualisation du marché" />
              </div>
            </div>
          </div>
        </section>

        <section id="marches" className="land-stats">
          <div className="land-stat">
            <span className="icon material-symbols-outlined">group</span>
            <h3>+10,000</h3>
            <p>Traders Actifs</p>
          </div>
          <div className="land-stat">
            <span className="icon material-symbols-outlined">account_balance</span>
            <h3>50+</h3>
            <p>Actifs Disponibles</p>
          </div>
          <div className="land-stat">
            <span className="icon material-symbols-outlined">security</span>
            <h3>0€</h3>
            <p>Risque Financier</p>
          </div>
        </section>

        <section id="fonctionnalites" className="land-features">
          <div className="land-features-head">
            <h2>L&apos;Outil Ultime d&apos;Apprentissage</h2>
            <p>
              Des données en direct aux analyses poussées, notre plateforme
              réplique l&apos;environnement des professionnels pour accélérer
              votre progression.
            </p>
          </div>
          <div className="features-grid">
            <div className="feature-card">
              <div className="f-icon">
                <span className="material-symbols-outlined">monitoring</span>
              </div>
              <h3>Simulation en temps réel</h3>
              <p>
                Accédez aux flux de données en direct des principaux marchés
                mondiaux. Passez des ordres au marché ou à cours limité avec une
                exécution simulée ultra-réaliste.
              </p>
            </div>
            <div className="feature-card">
              <div className="f-icon">
                <span className="material-symbols-outlined">pie_chart</span>
              </div>
              <h3>Gestion de portefeuille</h3>
              <p>
                Allouez votre capital virtuel stratégiquement. Visualisez la
                répartition de vos actifs avec des graphiques interactifs clairs
                et rééquilibrez en quelques clics.
              </p>
            </div>
            <div className="feature-card">
              <div className="f-icon">
                <span className="material-symbols-outlined">insights</span>
              </div>
              <h3>Suivi des performances</h3>
              <p>
                Analysez votre historique de trading avec des métriques
                professionnelles : ratio risque/récompense, drawdown maximum et
                statistiques de gains/pertes détaillées.
              </p>
            </div>
          </div>
        </section>
      </main>

      <footer className="land-footer">
        <div className="land-footer-inner">
          <div className="land-footer-brand">
            <img className="brand-img" src={logo} alt="XTrade" />
            XTrade
          </div>
          <nav className="land-footer-nav">
            <a href="#marches">Marchés</a>
            <a href="#fonctionnalites">Fonctionnalités</a>
            <a href="#hero">Sécurité</a>
            <a href="/login">Connexion</a>
            <a href="/signup">Inscription</a>
          </nav>
          <div className="land-footer-copy">
            © {new Date().getFullYear()} XTrade Financial. All rights reserved.
            Le trading comporte des risques.
          </div>
        </div>
      </footer>
    </div>
  )
}