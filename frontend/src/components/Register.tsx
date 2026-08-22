import React, { useState } from 'react'

interface RegisterProps {
  apiBase: string
  onRegisterSuccess: () => void
  onSwitchToLogin: () => void
}

export const Register: React.FC<RegisterProps> = ({ apiBase, onRegisterSuccess, onSwitchToLogin }) => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    roleId: 3 // Default 3 = GUEST, 2 = HOST
  })

  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'roleId' ? Number(value) : value
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setSuccess(null)
    setLoading(true)

    try {
      const response = await fetch(`${apiBase}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          username: formData.username.trim(),
          email: formData.email.trim(),
          password: formData.password,
          firstName: formData.firstName.trim(),
          lastName: formData.lastName.trim(),
          roleId: formData.roleId
        })
      })

      if (!response.ok) {
        const errorData = await response.json().catch(() => null)
        setError(errorData?.description || errorData?.message || 'Registration failed. Check your data.')
        setLoading(false)
        return
      }

      setSuccess('Account created successfully! You can now sign in.')
      setTimeout(() => {
        onRegisterSuccess()
      }, 1500)
    } catch {
      setError('Cannot connect to backend server. Make sure docker/backend is running.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-card">
      <div className="auth-header">
        <h2>Create an Account</h2>
        <p>Join as a Guest to book spaces or as a Host to list your venues</p>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit} className="form-stack">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="reg-firstName">First Name</label>
            <input
              id="reg-firstName"
              name="firstName"
              type="text"
              placeholder="e.g. John"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="reg-lastName">Last Name</label>
            <input
              id="reg-lastName"
              name="lastName"
              type="text"
              placeholder="e.g. Doe"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="reg-username">Username</label>
          <input
            id="reg-username"
            name="username"
            type="text"
            placeholder="e.g. johndoe"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="reg-email">Email Address</label>
          <input
            id="reg-email"
            name="email"
            type="email"
            placeholder="e.g. john@example.com"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="reg-password">Password</label>
          <input
            id="reg-password"
            name="password"
            type="password"
            placeholder="Min 6 characters"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="reg-role">I want to join as</label>
          <select id="reg-role" name="roleId" value={formData.roleId} onChange={handleChange}>
            <option value={3}>Guest (Browse & Book spaces)</option>
            <option value={2}>Host (List & Rent venues)</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
          {loading ? 'Creating Account...' : 'Sign Up'}
        </button>
      </form>

      <div className="auth-footer">
        <span>Already have an account? </span>
        <button type="button" className="btn-link" onClick={onSwitchToLogin}>
          Sign In
        </button>
      </div>
    </div>
  )
}

export default Register
