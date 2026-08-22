import { useState, useEffect } from 'react'
import Login from './components/Login'
import type { UserSession } from './components/Login'
import Register from './components/Register'

const API_BASE = 'http://localhost:8082/api/v1'

function App() {
  const [user, setUser] = useState<UserSession | null>(null)
  const [isRegisterMode, setIsRegisterMode] = useState(false)

  // Load existing session on component mount
  useEffect(() => {
    const savedUser = localStorage.getItem('peerspace_user')
    const savedToken = localStorage.getItem('peerspace_token')
    if (savedUser && savedToken) {
      try {
        const parsed = JSON.parse(savedUser)
        setUser(parsed)
      } catch {
        localStorage.removeItem('peerspace_user')
        localStorage.removeItem('peerspace_token')
      }
    }
  }, [])

  const handleLogout = () => {
    localStorage.removeItem('peerspace_token')
    localStorage.removeItem('peerspace_user')
    setUser(null)
    setIsRegisterMode(false)
  }

  const getRoleLabel = (roleId: number) => {
    switch (roleId) {
      case 1:
        return 'Admin'
      case 2:
        return 'Host'
      case 3:
        return 'Guest'
      default:
        return 'User'
    }
  }

  return (
    <div className="app-container">
      {/* Navigation Header */}
      <header className="app-header">
        <div className="brand">
          🏙️ <span>Peerspace</span>
        </div>

        {user && (
          <div className="user-badge">
            <span>
              Hello, <strong>{user.firstName || user.username}</strong>
            </span>
            <span className="role-pill">{getRoleLabel(user.roleId)}</span>
            <button className="btn btn-outline" onClick={handleLogout}>
              Sign Out
            </button>
          </div>
        )}
      </header>

      {/* Main Container */}
      <main className="main-content">
        {!user ? (
          // Auth Views: Login or Register
          isRegisterMode ? (
            <Register
              apiBase={API_BASE}
              onRegisterSuccess={() => setIsRegisterMode(false)}
              onSwitchToLogin={() => setIsRegisterMode(false)}
            />
          ) : (
            <Login
              apiBase={API_BASE}
              onLoginSuccess={(session) => setUser(session)}
              onSwitchToRegister={() => setIsRegisterMode(true)}
            />
          )
        ) : (
          // Authenticated Dashboard Landing View
          <div className="dashboard-view">
            <div className="dashboard-hero">
              <h1>Welcome to Peerspace, {user.firstName || user.username}!</h1>
              <p>You are signed in as a <strong>{getRoleLabel(user.roleId)}</strong>.</p>
              
              <div className="status-badge-container">
                <span className="status-dot"></span>
                <span>Connected to Backend API: <code>{API_BASE}</code></span>
              </div>
            </div>

            <div className="alert alert-success">
              ✅ Authentication and JWT Session successfully initialized!
            </div>
          </div>
        )}
      </main>
    </div>
  )
}

export default App
