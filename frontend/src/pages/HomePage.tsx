import { useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { getCurrentUser } from '../services/authApi'
import SignOutButton from '../components/SignOutButton'

export default function HomePage() {
  const [user, setUser] = useState<{ id: string; email: string } | null>(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    getCurrentUser().then((u) => {
      setUser(u)
      setLoading(false)
      if (!u) navigate('/sign-in', { replace: true })
    })
  }, [navigate])

  if (loading) return <div>Loading...</div>
  if (!user) return null

  return (
    <div style={{ padding: '2rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Welcome</h1>
        <SignOutButton />
      </div>
      <p>Signed in as {user.email}</p>
    </div>
  )
}
