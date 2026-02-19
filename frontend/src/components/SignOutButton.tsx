import { useNavigate } from 'react-router-dom'
import { signOut } from '../services/authApi'

export default function SignOutButton() {
  const navigate = useNavigate()

  async function handleClick() {
    await signOut()
    navigate('/sign-in', { replace: true })
  }

  return <button onClick={handleClick}>Sign Out</button>
}
