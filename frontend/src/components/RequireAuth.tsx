import type { ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { getSession } from '../utils/auth'

export default function RequireAuth({ children }: { children: ReactNode }) {
  const location = useLocation()
  if (!getSession()) {
    return <Navigate to="/login" replace state={{ from: location }} />
  }
  return <>{children}</>
}