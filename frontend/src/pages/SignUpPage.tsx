import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { signUp } from '../services/authApi'

const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/

export default function SignUpPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const passwordValid = PASSWORD_REGEX.test(password)
  const emailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')
    if (!passwordValid) {
      setError('Password must be at least 8 characters with one letter and one digit')
      return
    }
    setLoading(true)
    try {
      await signUp(email, password)
      navigate('/', { replace: true })
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Sign up failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: 400, margin: '2rem auto', padding: '1rem' }}>
      <h1>Sign Up</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            autoComplete="email"
            style={{ display: 'block', width: '100%', padding: '0.5rem' }}
          />
          {email && !emailValid && <small style={{ color: 'crimson' }}>Invalid email format</small>}
        </div>
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            autoComplete="new-password"
            minLength={8}
            style={{ display: 'block', width: '100%', padding: '0.5rem' }}
          />
          {password && !passwordValid && (
            <small style={{ color: 'crimson' }}>At least 8 characters, one letter, one digit</small>
          )}
        </div>
        {error && <p style={{ color: 'crimson', marginBottom: '1rem' }}>{error}</p>}
        <button type="submit" disabled={loading || !passwordValid}>
          {loading ? 'Creating account...' : 'Sign Up'}
        </button>
      </form>
      <p style={{ marginTop: '1rem' }}>
        <Link to="/sign-in">Sign in</Link> if you already have an account.
      </p>
    </div>
  )
}
