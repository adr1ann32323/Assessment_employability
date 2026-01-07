import { Link } from 'react-router-dom';

const Landing = () => {
  return (
    <div className="hero-section">
      {/* Navbar */}
      <nav className="navbar navbar-expand-lg navbar-dark py-3">
        <div className="container">
          <Link to="/" className="navbar-brand navbar-brand-custom">
            <i className="bi bi-kanban me-2"></i>
            ProjectHub
          </Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav ms-auto align-items-center">
              <li className="nav-item">
                <Link to="/login" className="nav-link text-white">
                  Sign In <i className="bi bi-arrow-right ms-1"></i>
                </Link>
              </li>
              <li className="nav-item ms-2">
                <Link to="/register" className="btn btn-light px-4">
                  Get Started
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      {/* Hero Content */}
      <div className="container">
        <div className="row align-items-center min-vh-75 py-5">
          <div className="col-lg-6 text-white">
            <h1 className="display-4 fw-bold mb-4">
              Why ProjectHub?
            </h1>
            <p className="lead mb-4 opacity-90">
              ProjectHub makes it easy for you to manage your projects and tasks efficiently,
              so you can focus on what matters and deliver results on time.
            </p>
            <div className="d-flex gap-3 mb-5">
              <Link to="/register" className="btn btn-light btn-lg px-4">
                See it in action
              </Link>
              <Link to="/login" className="btn btn-outline-light btn-lg px-4">
                Learn more
              </Link>
            </div>

            {/* Trust badges */}
            <div className="mt-5">
              <p className="small opacity-75 mb-3">Trusted by development teams worldwide</p>
              <div className="d-flex gap-4 align-items-center flex-wrap">
                <span className="badge bg-white text-dark px-3 py-2">
                  <i className="bi bi-award me-1"></i> Top Rated
                </span>
                <span className="badge bg-white text-dark px-3 py-2">
                  <i className="bi bi-shield-check me-1"></i> Secure
                </span>
                <span className="badge bg-white text-dark px-3 py-2">
                  <i className="bi bi-lightning me-1"></i> Fast
                </span>
                <span className="badge bg-white text-dark px-3 py-2">
                  <i className="bi bi-cloud-check me-1"></i> Cloud
                </span>
              </div>
            </div>
          </div>

          <div className="col-lg-6 mt-5 mt-lg-0">
            {/* Dashboard Preview Card */}
            <div className="card shadow-lg border-0 rounded-4">
              <div className="card-body p-4">
                <div className="d-flex align-items-center mb-4">
                  <div className="bg-primary rounded-circle p-2 me-3">
                    <i className="bi bi-kanban text-white"></i>
                  </div>
                  <div>
                    <h6 className="mb-0">Project Dashboard</h6>
                    <small className="text-muted">Real-time overview</small>
                  </div>
                </div>

                {/* Sample Projects */}
                <div className="list-group list-group-flush">
                  <div className="list-group-item d-flex justify-content-between align-items-center px-0">
                    <div className="d-flex align-items-center">
                      <i className="bi bi-folder text-primary me-3"></i>
                      <div>
                        <h6 className="mb-0">Website Redesign</h6>
                        <small className="text-muted">4 tasks</small>
                      </div>
                    </div>
                    <span className="badge bg-success">Active</span>
                  </div>
                  <div className="list-group-item d-flex justify-content-between align-items-center px-0">
                    <div className="d-flex align-items-center">
                      <i className="bi bi-folder text-primary me-3"></i>
                      <div>
                        <h6 className="mb-0">Mobile App</h6>
                        <small className="text-muted">8 tasks</small>
                      </div>
                    </div>
                    <span className="badge bg-warning text-dark">Draft</span>
                  </div>
                  <div className="list-group-item d-flex justify-content-between align-items-center px-0">
                    <div className="d-flex align-items-center">
                      <i className="bi bi-folder text-primary me-3"></i>
                      <div>
                        <h6 className="mb-0">API Integration</h6>
                        <small className="text-muted">12 tasks</small>
                      </div>
                    </div>
                    <span className="badge bg-success">Active</span>
                  </div>
                </div>

                {/* Stats */}
                <div className="row mt-4 text-center">
                  <div className="col-4">
                    <h4 className="text-primary mb-0">24</h4>
                    <small className="text-muted">Projects</small>
                  </div>
                  <div className="col-4">
                    <h4 className="text-success mb-0">156</h4>
                    <small className="text-muted">Tasks</small>
                  </div>
                  <div className="col-4">
                    <h4 className="text-warning mb-0">89%</h4>
                    <small className="text-muted">Complete</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="container pb-5">
        <div className="text-center text-white">
          <p className="mb-4 opacity-75">Join 1,700+ teams winning with collaborative project management</p>
          <div className="d-flex justify-content-center gap-4 flex-wrap opacity-50">
            <span className="fw-bold">TechCorp</span>
            <span className="fw-bold">StartupX</span>
            <span className="fw-bold">DevTeam</span>
            <span className="fw-bold">CloudBase</span>
            <span className="fw-bold">DataFlow</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Landing;

