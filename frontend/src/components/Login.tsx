import React, { useState } from 'react'

export interface UserSession {
  token: string
  userId: number
  username: string
  firstName: string
  lastName: string
  roleId: number // 2 = HOST, 3 = GUEST, 1 = ADMIN
}

interface LoginProps {
  apiBase: string
  onLoginSuccess: (user: UserSession) => void
  onSwitchToRegister: () => void
}

export const Login: React.FC<LoginProps> = ({ apiBase, onLoginSuccess, onSwitchToRegister }) => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    try {
      const response = await fetch(`${apiBase}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: email.trim(),
          password: password
        })
      })

      if (!response.ok) {
        setError('Invalid email or password. Please try again.')
        setLoading(false)
        return
      }

      const data = await response.json()
      // Expected backend response: { token: string, roleId?: number, userId?: number, username?: string, ... }
      let token = data.token || ''
      let roleId = data.roleId || 3
      let userId = data.userId || 1
      let username = data.username || email.split('@')[0]
      let firstName = data.firstName || 'User'
      let lastName = data.lastName || ''

      // If JWT contains payload data, we can also extract role from it
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]))
          if (payload.userId) userId = payload.userId
          if (payload.role) {
            if (payload.role === 'ROLE_HOST' || payload.role === 'HOST') roleId = 2
            if (payload.role === 'ROLE_GUEST' || payload.role === 'GUEST') roleId = 3
            if (payload.role === 'ROLE_ADMIN' || payload.role === 'ADMIN') roleId = 1
          }
          if (payload.sub && !username) username = payload.sub
        } catch {
          // Token decode fallback
        }
      }

      const session: UserSession = {
        token,
        userId,
        username,
        firstName,
        lastName,
        roleId
      }

      localStorage.setItem('peerspace_token', token)
      localStorage.setItem('peerspace_user', JSON.stringify(session))
      onLoginSuccess(session)
    } catch {
      setError('Cannot connect to backend server. Make sure docker/backend is running on port 8082.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-card">
      <div className="auth-header">
        <h2>Welcome Back</h2>
        <p>Sign in to manage your spaces and bookings</p>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      <form onSubmit={handleSubmit} className="form-stack">
        <div className="form-group">
          <label htmlFor="login-email">Email Address</label>
          <input
            id="login-email"
            type="email"
            placeholder="e.g. host@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="login-password">Password</label>
          <input
            id="login-password"
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
          {loading ? 'Signing in...' : 'Sign In'}
        </button>
      </form>

      <div className="auth-footer">
        <span>Don't have an account? </span>
        <button type="button" className="btn-link" onClick={onSwitchToRegister}>
          Create an account
        </button>
      </div>
    </div>
  )
}

export default Login
