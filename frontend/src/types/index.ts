export type AssetType = 'STOCK' | 'CRYPTO'

export interface Trader {
  id: number
  nom: string
  balance: number
  valeurPortefeuille: number
  valeurTotale: number
}

export interface Asset {
  id: number
  code: string
  nom: string
  prixUnitaire: number
  type: AssetType
}

export interface Stats {
  tradersCount: number
  assetsCount: number
  transactionsCount: number
  totalAchats: number
  totalVentes: number
  volumeParActif: Record<string, number>
}

export interface PortfolioItem {
  assetId: number
  code: string
  nom: string
  type: AssetType
  prixUnitaire: number
  quantite: number
  valeur: number
}

export interface Portfolio {
  traderId: number
  traderNom: string
  solde: number
  valeurPortefeuille: number
  valeurTotale: number
  actifs: PortfolioItem[]
}

export type TransactionType = 'ACHAT' | 'VENTE'

export interface Transaction {
  id: number
  type: TransactionType
  traderId: number
  traderNom: string
  assetId: number
  assetCode: string
  assetNom: string
  quantite: number
  prixUnitaire: number
  montantTotal: number
  frais: number
  date: string
}

export type OrderType = 'LIMIT' | 'STOP_LOSS' | 'TAKE_PROFIT'
export type OrderDirection = 'BUY' | 'SELL'
export type OrderStatus = 'PENDING' | 'EXECUTED' | 'CANCELLED'

export interface Order {
  id: number
  traderId: number
  traderNom: string
  assetId: number
  assetCode: string
  assetNom: string
  type: OrderType
  direction: OrderDirection
  quantite: number
  prixDeclaration: number
  statut: OrderStatus
  datePlacement: string
  dateExecution: string | null
}

export interface OrderRequest {
  traderId: number
  assetId: number
  type: OrderType
  direction: OrderDirection
  quantite: number
  prixDeclaration: number
}

export interface Performance {
  traderId: number
  traderNom: string
  solde: number
  capitalInvestiTotal: number
  montantRecupereTotal: number
  gainsPertesRealises: number
  valeurPortefeuilleActuel: number
  gainsPertesLatents: number
  performancePourcentage: number
  transactions: Transaction[]
}

export interface TradeRequest {
  traderId: number
  assetId: number
  quantite: number
}

export interface TraderRequest {
  nom: string
  balance: number
}

export interface AssetRequest {
  code: string
  nom: string
  prixUnitaire: number
  type: AssetType
}

export interface AuthResponse {
  token: string
  type: string
  traderId: number
  nom: string
  email: string
  role: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  nom: string
  email: string
  password: string
  balance: number
}
