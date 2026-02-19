import { useEffect, useState } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { getCurrentUser } from '../services/authApi'

interface Props {
  children: React.ReactNode
}

export default function ProtectedRoute({ children }: Props) {
  const [status, setStatus] = useState<'loading' | 'auth' | 'unauth'>('loading')
  const location = useLocation()

  useEffect(() => {
    getCurrentUser().then((user) => {
      setStatus(user ? 'auth' : 'unauth')
    })
  }, [])

  if (status === 'loading') return <div>Loading...</div>
  if (status === 'unauth') return <Navigate to="/sign-in" state={{ from: location }} replace />

  return <>{children}</>
}
