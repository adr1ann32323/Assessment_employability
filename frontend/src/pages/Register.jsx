import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const Register = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (password.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }

    setLoading(true);

    try {
      const response = await authService.register(username, email, password);
      login(response.data);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container d-flex">
      {/* Left Side - Branding */}
      <div className="auth-left d-none d-lg-flex col-lg-6 flex-column justify-content-center align-items-center text-white p-5">
        <div className="text-center">
          <i className="bi bi-rocket-takeoff display-1 mb-4"></i>
          <h2 className="fw-bold mb-3">Start Your Journey</h2>
          <p className="lead opacity-90 mb-4">
            Create an account and start managing your projects like a pro.
          </p>
          <ul className="list-unstyled text-start">
            <li className="mb-3 d-flex align-items-center">
              <i className="bi bi-check-circle-fill me-3"></i>
              Unlimited projects and tasks
            </li>
            <li className="mb-3 d-flex align-items-center">
              <i className="bi bi-check-circle-fill me-3"></i>
              Real-time collaboration
            </li>
            <li className="mb-3 d-flex align-items-center">
              <i className="bi bi-check-circle-fill me-3"></i>
              Secure and reliable
            </li>
            <li className="d-flex align-items-center">
              <i className="bi bi-check-circle-fill me-3"></i>
              Free forever for personal use
            </li>
          </ul>
        </div>
      </div>

      {/* Right Side - Register Form */}
      <div className="col-12 col-lg-6 d-flex flex-column justify-content-center align-items-center p-5 bg-white">
        <div className="w-100" style={{ maxWidth: '400px' }}>
          <div className="text-center mb-4">
            <Link to="/" className="text-decoration-none">
              <i className="bi bi-kanban text-primary display-6"></i>
              <h4 className="text-dark mt-2">ProjectHub</h4>
            </Link>
          </div>

          <h2 className="text-center mb-2">Create Account</h2>
          <p className="text-muted text-center mb-4">Fill in your details to get started</p>

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
                  placeholder="Choose a username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="mb-3">
              <label htmlFor="email" className="form-label">Email</label>
              <div className="input-group">
                <span className="input-group-text">
                  <i className="bi bi-envelope"></i>
                </span>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="mb-3">
              <label htmlFor="password" className="form-label">Password</label>
              <div className="input-group">
                <span className="input-group-text">
                  <i className="bi bi-lock"></i>
                </span>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  placeholder="Create a password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="mb-4">
              <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
              <div className="input-group">
                <span className="input-group-text">
                  <i className="bi bi-lock-fill"></i>
                </span>
                <input
                  type="password"
                  className="form-control"
                  id="confirmPassword"
                  placeholder="Confirm your password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
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
                  Creating account...
                </>
              ) : (
                <>
                  Create Account <i className="bi bi-arrow-right ms-2"></i>
                </>
              )}
            </button>
          </form>

          <p className="text-center text-muted mb-0">
            Already have an account?{' '}
            <Link to="/login" className="text-primary text-decoration-none fw-medium">
              Sign In
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;

