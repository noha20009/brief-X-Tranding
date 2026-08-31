export const formatCurrency = (value: number): string =>
  new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(value)

export const formatCompact = (value: number): string =>
  new Intl.NumberFormat('fr-FR', {
    notation: 'compact',
    maximumFractionDigits: 1,
  }).format(value)

export const formatPercent = (value: number): string =>
  `${value >= 0 ? '+' : ''}${value.toFixed(2)} %`

export const formatDate = (iso: string): string =>
  new Date(iso).toLocaleString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })

export const colorFromAssetType = (type: string): string =>
  type === 'CRYPTO' ? 'var(--crypto)' : 'var(--stock)'

export const initials = (name: string): string =>
  name
    .split(' ')
    .filter(Boolean)
    .map((p) => p[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
