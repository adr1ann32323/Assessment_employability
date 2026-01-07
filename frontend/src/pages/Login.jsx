import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await authService.login(username, password);
      login(response.data);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container d-flex">
      {/* Left Side - Branding */}
      <div className="auth-left d-none d-lg-flex col-lg-6 flex-column justify-content-center align-items-center text-white p-5">
        <div className="text-center">
          <i className="bi bi-kanban display-1 mb-4"></i>
          <h2 className="fw-bold mb-3">Welcome Back!</h2>
          <p className="lead opacity-90 mb-4">
            Manage your projects efficiently and keep your team on track.
          </p>
          <div className="d-flex justify-content-center gap-3">
            <div className="text-center">
              <h3 className="fw-bold">500+</h3>
              <small className="opacity-75">Projects</small>
            </div>
            <div className="text-center">
              <div className="vr h-100 mx-3 opacity-50"></div>
            </div>
            <div className="text-center">
              <h3 className="fw-bold">10K+</h3>
              <small className="opacity-75">Tasks</small>
            </div>
            <div className="text-center">
              <div className="vr h-100 mx-3 opacity-50"></div>
            </div>
            <div className="text-center">
              <h3 className="fw-bold">99%</h3>
              <small className="opacity-75">Uptime</small>
            </div>
          </div>
        </div>
      </div>

      {/* Right Side - Login Form */}
      <div className="col-12 col-lg-6 d-flex flex-column justify-content-center align-items-center p-5 bg-white">
        <div className="w-100" style={{ maxWidth: '400px' }}>
          <div className="text-center mb-4">
            <Link to="/" className="text-decoration-none">
              <i className="bi bi-kanban text-primary display-6"></i>
              <h4 className="text-dark mt-2">ProjectHub</h4>
            </Link>
          </div>

          <h2 className="text-center mb-2">Sign In</h2>
          <p className="text-muted text-center mb-4">Enter your credentials to access your account</p>

          {error && (
            <div className="alert alert-danger d-flex align-items-center" role="alert">
              <i className="bi bi-exclamation-triangle-fill me-2"></i>
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">Username</label>
              <div className="input-group">
                <span className="input-group-text">
                  <i className="bi bi-person"></i>
                </span>
                <input
                  type="text"
                  className="form-control"
                  id="username"
                  placeholder="Enter your username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="mb-4">
              <label htmlFor="password" className="form-label">Password</label>
              <div className="input-group">
                <span className="input-group-text">
                  <i className="bi bi-lock"></i>
                </span>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary w-100 py-2 mb-3"
              disabled={loading}
            >
              {loading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                  Signing in...
                </>
              ) : (
                <>
                  Sign In <i className="bi bi-arrow-right ms-2"></i>
                </>
              )}
            </button>
          </form>

          <p className="text-center text-muted mb-0">
            Don't have an account?{' '}
            <Link to="/register" className="text-primary text-decoration-none fw-medium">
              Sign Up
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;

