import axios from 'axios'
import type {
  Asset,
  AssetRequest,
  AuthResponse,
  LoginRequest,
  Order,
  OrderRequest,
  Performance,
  Portfolio,
  RegisterRequest,
  Stats,
  TradeRequest,
  Trader,
  TraderRequest,
  Transaction,
} from '../types'
import { clearSession, getSession } from '../utils/auth'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  const session = getSession()
  if (session?.token) {
    config.headers.Authorization = `Bearer ${session.token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && window.location.pathname !== '/login') {
      clearSession()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  },
)

export function getErrorMessage(err: unknown, fallback: string): string {
  if (axios.isAxiosError(err)) {
    const data = err.response?.data as { message?: string } | undefined
    if (data?.message) return data.message
  }
  return fallback
}

export const login = (req: LoginRequest) =>
  api.post<AuthResponse>('/auth/login', req).then((r) => r.data)

export const register = (req: RegisterRequest) =>
  api.post<AuthResponse>('/auth/signup', req).then((r) => r.data)

export const fetchStats = () => api.get<Stats>('/stats').then((r) => r.data)

export const fetchTraders = () => api.get<Trader[]>('/traders').then((r) => r.data)

export const fetchTrader = (id: number) =>
  api.get<Trader>(`/traders/${id}`).then((r) => r.data)

export const fetchPortfolio = (id: number) =>
  api.get<Portfolio>(`/traders/${id}/portfolio`).then((r) => r.data)

export const fetchPerformance = (id: number) =>
  api.get<Performance>(`/stats/trader/${id}`).then((r) => r.data)

export const createTrader = (req: TraderRequest) =>
  api.post<Trader>('/traders', req).then((r) => r.data)

export const fetchAssets = () => api.get<Asset[]>('/assets').then((r) => r.data)

export const createAsset = (req: AssetRequest) =>
  api.post<Asset>('/assets', req).then((r) => r.data)

export const updateAssetPrice = (id: number, prix: number) =>
  api.put<Asset>(`/assets/${id}`, null, { params: { prix } }).then((r) => r.data)

export const buyAsset = (req: TradeRequest) =>
  api.post<Transaction>('/trades/buy', req).then((r) => r.data)

export const sellAsset = (req: TradeRequest) =>
  api.post<Transaction>('/trades/sell', req).then((r) => r.data)

export const placeOrder = (req: OrderRequest) =>
  api.post<Order>('/orders', req).then((r) => r.data)

export const fetchOrders = (traderId: number) =>
  api.get<Order[]>(`/orders/trader/${traderId}`).then((r) => r.data)

export const cancelOrder = (orderId: number, traderId: number) =>
  api.delete<Order>(`/orders/${orderId}`, { params: { traderId } }).then((r) => r.data)

export function downloadExport(
  format: 'csv' | 'excel',
  traderId: number,
): Promise<void> {
  const session = getSession()
  if (!session?.token) return Promise.reject(new Error('Non authentifié'))

  const filename = format === 'csv' ? 'transactions.csv' : 'transactions.xlsx'
  const url = `/api/export/${format}?traderId=${traderId}`

  return fetch(url, { headers: { Authorization: `Bearer ${session.token}` } })
    .then((res) => {
      if (!res.ok) throw new Error(`Erreur export (${res.status})`)
      return res.blob()
    })
    .then((blob) => {
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = filename
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(link.href)
    })
}
