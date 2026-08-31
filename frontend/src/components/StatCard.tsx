interface StatCardProps {
  label: string
  value: string
  sub?: string
  icon?: string
  accent?: 'green' | 'red' | 'blue' | 'purple'
}

export default function StatCard({
  label,
  value,
  sub,
  icon,
  accent = 'blue',
}: StatCardProps) {
  return (
    <div className={`stat-card accent-${accent}`}>
      {(icon || label) && (
        <div className="stat-top" aria-hidden="true">
          {icon && (
            <span className="stat-icon material-symbols-outlined">{icon}</span>
          )}
          <span className="stat-label">{label}</span>
        </div>
      )}
      <span className="stat-value">{value}</span>
      {sub && <span className="stat-sub">{sub}</span>}
    </div>
  )
}