# XTrade

Plateforme de trading de simulation (backend + frontend).

---

# 1. Nom du projet

**Nom du projet :** XTrade

---

# 2. Présentation du projet

Ce projet est une **plateforme web de trading de simulation** qui permet d'acheter et de vendre des actifs financiers (actions et crypto-monnaies) avec un capital virtuel, en temps réel.

Il s'adresse principalement aux **étudiants, débutants en finance et développeurs** qui souhaitent s'entraîner à prendre des décisions de trading sans risque financier ni argent réel.

Son objectif principal est de **reproduire les conditions réelles du marché** — cotations en temps réel, ordres conditionnels, frais et spread — afin de comprendre la gestion d'un portefeuille avant d'investir pour de vrai.

Cette application est constituée d'un **backend Spring Boot** (API REST, sécurité JWT, WebSocket) et d'un **frontend React** (interface utilisateur moderne et responsive).

---

# 3. Problématique

Le problème identifié est que **les débutants n'ont pas d'environnement simple et sécurisé pour s'entraîner au trading** : les simulateurs professionnels sont complexes, et un apprentissage sur un vrai compte expose à des pertes financières.

La solution proposée permet de **simuler l'ensemble du cycle de trading** — consulter des cotations temps réel, acheter, vendre, placer des ordres conditionnels avec frais et spread — le tout avec un capital virtuel et des données de marché réalistes.

Ainsi, l'utilisateur peut **apprendre le fonctionnement des marchés** et développer ses stratégies sans prendre aucun risque réel.

---

# 4. Fonctionnalités principales

- Créer et gérer des comptes traders (inscription / connexion sécurisée)
- Consulter les cotations en temps réel des actifs (actions & crypto-monnaies)
- Acheter et vendre des actifs avec frais de transaction et spread simulés
- Placer des ordres conditionnels (limite, stop-loss, take-profit) auto-exécutés
- Consulter le portefeuille et les statistiques de performance
- Exporter l'historique des transactions en CSV ou Excel

---

# 5. Technologies utilisées

| Technologie | Utilisation dans le projet |
|-------------|----------------------------|
| Java 17 | Langage de programmation du backend |
| Spring Boot 3.3.4 | Framework du backend (API REST, injection de dépendances) |
| Spring Data JPA / Hibernate | Accès et persistance des données en base MySQL |
| Spring Security + JWT | Authentification et autorisation des utilisateurs |
| Spring WebSocket | Diffusion des cotations en temps réel |
| OpenCSV & Apache POI | Génération des fichiers d'export (CSV et Excel) |
| MySQL | Base de données relationnelle |
| React 19 | Développement de l'interface utilisateur |
| TypeScript | Typage statique du frontend |
| Vite | Serveur de développement et build du frontend |
| React Router | Gestion de la navigation entre les pages |
| Recharts | Visualisation des données (graphiques de performance) |
| Axios | Appels HTTP vers l'API backend |
| Git / GitHub | Versionnement et hébergement du code |

> Nous avons utilisé **Spring Boot** pour construire une API REST robuste et sécurisée, et **React avec TypeScript** pour offrir une interface utilisateur réactive et agréable, connectée en temps réel au backend via **WebSocket**.

---

# 6. Installation et lancement

## 6.1 Prérequis

Pour utiliser ce projet, vous devez disposer de :

- **Java 17** (JDK)
- **Maven 3.9+**
- **Node.js 18+** et **npm**
- **MySQL 8** (serveur de base de données)
- **Git**
- IDE recommandé : **VS Code** (frontend) ou **IntelliJ IDEA** (backend)

---

## 6.2 Cloner le dépôt

```bash
git clone https://github.com/noha20009/brief-X-Tranding.git
```

---

## 6.3 Ouvrir le dossier

```bash
cd brief-X-Tranding
```

---

## 6.4 Installer les dépendances

### Backend

```bash
cd backend
mvn clean install
```

### Frontend

```bash
cd frontend
npm install
```

---

## 6.5 Variables d'environnement

Le backend lit sa configuration via des variables d'environnement (avec des valeurs par défaut).

Variables de votre projet :

```env
DB_URL=jdbc:mysql://localhost:3306/xtrade?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=VOTRE_MOT_DE_PASSE_MYSQL
JWT_SECRET=UNE_CLE_SECRETE_BASE64_DE_256_BITS_MINIMUM
JWT_EXPIRATION_MS=86400000
```

> ⚠️ **Sécurité :** ne publiez jamais votre mot de passe réel ni vos clés secrètes. Utilisez les variables d'environnement ou un fichier `.env` non versionné.

---

## 6.6 Lancer le projet

### Backend (sur le port 8081)

```bash
cd backend
mvn spring-boot:run
```

### Frontend (sur le port 5173)

Dans un autre terminal :

```bash
cd frontend
npm run dev
```

---

## 6.7 Ouvrir le projet

Après le lancement du frontend :

```
http://localhost:5173
```

> L'API backend est accessible sur `http://localhost:8081` (le frontend y accède via un proxy Vite).

### Point de vigilance

- Tester toutes les commandes dans un terminal ouvert sur le bon dossier (`backend/` et `frontend/` séparément)
- Vérifier les chemins et les ports (8081 pour l'API, 5173 pour le frontend)
- Le backend doit être démarré **avant** le frontend pour que les données s'affichent
- Ne jamais publier de mots de passe ou de clés API dans le code versionné

---

# 7. Captures d'écran

## Capture 1

### Titre

```
Page de connexion XTrade
```

### Image

```md
![Page de connexion XTrade](captures/login.png)
```

> Placez votre capture dans un dossier `captures/` à la racine du projet et ajustez le chemin.

### Explication

Cette capture montre la page d'authentification sécurisée par JWT à partir de laquelle l'utilisateur se connecte pour accéder à sa session de trading.

---

## Capture 2

### Titre

```
Tableau de bord d'un trader
```

### Image

```md
![Tableau de bord trader](captures/dashboard.png)
```

### Explication

Cette capture montre le portefeuille d'un trader : sa balance, la valeur de ses actifs, sa performance, ainsi que l'historique de ses transactions.

---

## Capture 3

### Titre

```
Ordre conditionnel et cotations temps réel
```

### Image

```md
![Ordre conditionnel](captures/orders.png)
```

### Explication

Cette capture illustre la pose d'un ordre conditionnel (take-profit sur Bitcoin) et la liste des ordres avec leur statut, ainsi que les cotations mises à jour en temps réel.

---

# 8. Contribution personnelle

> ⚠️ **À personnaliser** selon votre participation au projet (rubrique obligatoire pour les projets de groupe).

Ma contribution principale a porté sur la **création du backend Spring Boot** : modèles de données, API REST, authentification JWT, ordres conditionnels, frais/spread, exports CSV & Excel et cotations WebSocket temps réel.

J'ai également travaillé sur le **frontend React** : gestion des états et des formulaires, connexion temps réel aux cotations et intégration de la page de détails d'un trader.

J'ai été responsable de la **conception de la base de données MySQL** et de l'**orchestration du lancement** (démarrage backend + frontend, gestion des ports et des conflits).

---

# 9. Difficultés rencontrées

## Difficulté 1 — Conflit de port lors du lancement du backend

### Problème rencontré
Le backend affichait l'erreur `Web server failed to start. Port 8081 was already in use` lorsque je tentais de le relancer depuis mon IDE, alors qu'une instance précédente tournait encore en arrière-plan.

### Recherches / Tests
J'ai vérifié le processus écoutant sur le port 8081 (via `Get-NetTCPConnection`) et identifié qu'une instance Maven `spring-boot:run` lancée en tâche de fond occupait toujours le port.

### Solution
J'ai arrêté l'instance en arrière-plan et libéré le port 8081 avant de relancer l'application depuis l'IDE.

### Ce que j'ai appris
J'ai appris à diagnostiquer les conflits de ports et à gérer les processus associés aux applications Spring Boot.

### Texte final
J'ai rencontré le problème suivant : le port 8081 était déjà occupé lors du lancement. Pour comprendre l'origine du problème, j'ai identifié le processus écoutant sur le port concerné. J'ai résolu le problème en arrêtant l'instance en arrière-plan. Cette difficulté m'a permis d'apprendre à gérer les conflits de ports dans un environnement de développement.

---

## Difficulté 2 — Packaging du backend bloqué (jar verrouillé)

### Problème rencontré
La commande `mvn package` échouait à l'étape `repackage` avec une erreur de renommage du fichier `.jar`, alors que la compilation réussissait.

### Recherches / Tests
J'ai constaté que la compilation passait et que l'échec survenait uniquement au moment de la génération du fichier exécutable, signe d'un fichier verrouillé par un processus en cours.

### Solution
J'ai arrêté les instances de l'application qui maintenaient le fichier verrouillé, puis relancé la compilation/le packaging proprement.

### Ce que j'ai appris
J'ai compris l'importance de stopper les processus avant de repackager un artefact et le rôle du plugin `spring-boot-maven-plugin`.

---

# 10. Améliorations possibles

Dans une prochaine version, je pourrais :

- améliorer la sécurité (gestion plus fine des rôles, renouvellement des jetons)
- ajouter des tests automatisés (unitaires et d'intégration) au backend et au frontend
- rendre l'interface entièrement responsive et accessible
- déployer l'application (frontend sur un hébergeur statique, backend sur un cloud type AWS/Heroku)
- alimenter les cotations avec des données de marché réelles via une API externe

### Conclusion

Ces améliorations permettraient de **professionnaliser davantage la plateforme**, de garantir sa robustesse par des tests, et de rendre l'entraînement au trading encore plus réaliste et accessible partout.

---

# ✅ Checklist finale

## Présentation
- [x] Le nom du projet est clair.
- [x] Le projet est présenté en 3 à 5 lignes.
- [x] Le public cible est identifié.
- [x] Le besoin est expliqué.
- [x] L'objectif est précisé.

## Fonctionnalités
- [x] 3 à 6 fonctionnalités.
- [x] Chaque fonctionnalité commence par un verbe.
- [x] Elles correspondent à des actions réelles.

## Technologies
- [x] Les technologies sont indiquées.
- [x] Leur rôle est expliqué.

## Installation
- [x] Les prérequis sont présents.
- [x] Le dépôt est correct.
- [x] Les commandes fonctionnent.
- [ ] L'adresse locale est indiquée. *(→ indiquée en 6.7)*
- [x] Aucune donnée sensible n'est publiée.

## Captures
- [ ] Deux captures minimum. *(→ à ajouter vos images dans le dossier captures/)*
- [ ] Chaque capture possède un titre.
- [ ] Les images fonctionnent.

## Contribution
- [x] Ma contribution est précise. *(→ à personnaliser)*
- [ ] Les tâches sont clairement décrites.
- [ ] Je distingue mon travail de celui du groupe.

## Difficultés
- [x] Les difficultés sont expliquées.
- [x] Les recherches sont décrites.
- [x] Les solutions sont précisées.
- [x] Les apprentissages sont présentés.

## Améliorations
- [x] 2 à 4 améliorations.
- [x] Elles sont réalistes.

---

# Bonus — Poste LinkedIn (prêt à poster)

### 🚀 J'ai construit une plateforme de trading qui ne fait JAMAIS perdre un centime réel — et voici ce que j'ai appris.

J'ai livré **XTrade** : un environnement complet de simulation de marché pour s'entraîner au trading dans des conditions professionnelles, avec du **capital virtuel**.

### ⚙️ Ce que fait la plateforme
- 📈 **Cotations en temps réel** (WebSocket)
- 🛑 **Ordres conditionnels** : limite, stop-loss, take-profit
- 💸 **Frais de transaction + spread** simulés
- 👤 **Authentification JWT** avec rôles
- 📊 **Dashboard** : portefeuille, performance, historique
- 📤 **Exports CSV & Excel**

### 🧰 Stack
**Backend** : Java 17 · Spring Boot 3.3.4 (Web, Data JPA, Security, WebSocket) · JWT (JJWT 0.12.6) · MySQL · H2 · OpenCSV · Apache POI · Lombok
**Frontend** : React 19 · TypeScript 6 · Vite 8 · React Router 7 · Recharts · Axios · Oxlint · Material Symbols

`#Java #SpringBoot #React #TypeScript #Finance #Trading #WebSocket #JWT #MySQL #Dev`
