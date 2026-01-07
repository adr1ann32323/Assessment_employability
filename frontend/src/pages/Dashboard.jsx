import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { projectService, taskService } from '../services/api';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Modal states
  const [showProjectModal, setShowProjectModal] = useState(false);
  const [showTaskModal, setShowTaskModal] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [newProjectName, setNewProjectName] = useState('');
  const [newTaskTitle, setNewTaskTitle] = useState('');
  const [tasks, setTasks] = useState({});
  const [expandedProject, setExpandedProject] = useState(null);

  // Generate avatar URL using UI Avatars service
  const getAvatarUrl = (name) => {
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=4f46e5&color=fff&size=128`;
  };

  useEffect(() => {
    loadProjects();
  }, []);

  const loadProjects = async () => {
    try {
      setLoading(true);
      const response = await projectService.getAll();
      setProjects(response.data);
    } catch (err) {
      setError('Failed to load projects');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadTasks = async (projectId) => {
    try {
      const response = await taskService.getByProject(projectId);
      setTasks(prev => ({ ...prev, [projectId]: response.data }));
    } catch (err) {
      console.error('Failed to load tasks', err);
    }
  };

  const handleCreateProject = async (e) => {
    e.preventDefault();
    try {
      await projectService.create(newProjectName);
      setNewProjectName('');
      setShowProjectModal(false);
      loadProjects();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create project');
    }
  };

  const handleActivateProject = async (projectId) => {
    try {
      await projectService.activate(projectId);
      loadProjects();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to activate project');
    }
  };

  const handleCreateTask = async (e) => {
    e.preventDefault();
    try {
      await taskService.create(selectedProject.id, newTaskTitle);
      setNewTaskTitle('');
      setShowTaskModal(false);
      loadTasks(selectedProject.id);
      loadProjects();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create task');
    }
  };

  const handleCompleteTask = async (taskId, projectId) => {
    try {
      await taskService.complete(taskId);
      loadTasks(projectId);
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to complete task');
    }
  };

  const toggleProjectExpand = async (project) => {
    if (expandedProject === project.id) {
      setExpandedProject(null);
    } else {
      setExpandedProject(project.id);
      if (!tasks[project.id]) {
        await loadTasks(project.id);
      }
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const openTaskModal = (project) => {
    setSelectedProject(project);
    setShowTaskModal(true);
  };

  return (
    <div className="min-vh-100 bg-light">
      {/* Navbar */}
      <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div className="container">
          <Link to="/dashboard" className="navbar-brand navbar-brand-custom text-primary">
            <i className="bi bi-kanban me-2"></i>
            ProjectHub
          </Link>

          <div className="d-flex align-items-center">
            <div className="dropdown">
              <button
                className="btn btn-link text-decoration-none d-flex align-items-center"
                type="button"
                data-bs-toggle="dropdown"
              >
                <img
                  src={getAvatarUrl(user?.username || 'User')}
                  alt="Avatar"
                  className="avatar me-2"
                />
                <span className="text-dark fw-medium d-none d-md-inline">
                  {user?.username}
                </span>
                <i className="bi bi-chevron-down ms-2 text-muted"></i>
              </button>
              <ul className="dropdown-menu dropdown-menu-end">
                <li>
                  <div className="dropdown-item-text">
                    <div className="d-flex align-items-center">
                      <img
                        src={getAvatarUrl(user?.username || 'User')}
                        alt="Avatar"
                        className="avatar-lg me-3"
                      />
                      <div>
                        <h6 className="mb-0">{user?.username}</h6>
                        <small className="text-muted">Project Manager</small>
                      </div>
                    </div>
                  </div>
                </li>
                <li><hr className="dropdown-divider" /></li>
                <li>
                  <button className="dropdown-item text-danger" onClick={handleLogout}>
                    <i className="bi bi-box-arrow-right me-2"></i>
                    Sign Out
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container py-4">
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h2 className="mb-1">Welcome back, {user?.username}!</h2>
            <p className="text-muted mb-0">Here's an overview of your projects</p>
          </div>
          <button
            className="btn btn-primary"
            onClick={() => setShowProjectModal(true)}
          >
            <i className="bi bi-plus-lg me-2"></i>
            New Project
          </button>
        </div>

        {/* Error Alert */}
        {error && (
          <div className="alert alert-danger alert-dismissible fade show" role="alert">
            {error}
            <button type="button" className="btn-close" onClick={() => setError('')}></button>
          </div>
        )}

        {/* Stats Cards */}
        <div className="row mb-4">
          <div className="col-md-4 mb-3">
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <div className="d-flex align-items-center">
                  <div className="bg-primary bg-opacity-10 rounded-3 p-3 me-3">
                    <i className="bi bi-folder text-primary fs-4"></i>
                  </div>
                  <div>
                    <h3 className="mb-0">{projects.length}</h3>
                    <small className="text-muted">Total Projects</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-md-4 mb-3">
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <div className="d-flex align-items-center">
                  <div className="bg-success bg-opacity-10 rounded-3 p-3 me-3">
                    <i className="bi bi-check-circle text-success fs-4"></i>
                  </div>
                  <div>
                    <h3 className="mb-0">{projects.filter(p => p.status === 'ACTIVE').length}</h3>
                    <small className="text-muted">Active Projects</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-md-4 mb-3">
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <div className="d-flex align-items-center">
                  <div className="bg-warning bg-opacity-10 rounded-3 p-3 me-3">
                    <i className="bi bi-file-earmark text-warning fs-4"></i>
                  </div>
                  <div>
                    <h3 className="mb-0">{projects.filter(p => p.status === 'DRAFT').length}</h3>
                    <small className="text-muted">Draft Projects</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Projects List */}
        <div className="card border-0 shadow-sm">
          <div className="card-header bg-white py-3">
            <h5 className="mb-0">
              <i className="bi bi-kanban me-2"></i>
              My Projects
            </h5>
          </div>
          <div className="card-body p-0">
            {loading ? (
              <div className="text-center py-5">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Loading...</span>
                </div>
              </div>
            ) : projects.length === 0 ? (
              <div className="text-center py-5">
                <i className="bi bi-folder2-open display-1 text-muted"></i>
                <h5 className="mt-3 text-muted">No projects yet</h5>
                <p className="text-muted">Create your first project to get started</p>
                <button
                  className="btn btn-primary"
                  onClick={() => setShowProjectModal(true)}
                >
                  <i className="bi bi-plus-lg me-2"></i>
                  Create Project
                </button>
              </div>
            ) : (
              <div className="list-group list-group-flush">
                {projects.map((project) => (
                  <div key={project.id} className="list-group-item">
                    <div
                      className="d-flex justify-content-between align-items-center py-2"
                      style={{ cursor: 'pointer' }}
                      onClick={() => toggleProjectExpand(project)}
                    >
                      <div className="d-flex align-items-center">
                        <i className={`bi ${expandedProject === project.id ? 'bi-chevron-down' : 'bi-chevron-right'} me-3 text-muted`}></i>
                        <i className="bi bi-folder-fill text-primary me-3 fs-5"></i>
                        <div>
                          <h6 className="mb-0">{project.name}</h6>
                          <small className="text-muted">
                            Created {new Date(project.createdAt).toLocaleDateString()}
                          </small>
                        </div>
                      </div>
                      <div className="d-flex align-items-center gap-2">
                        <span className={`badge ${project.status === 'ACTIVE' ? 'bg-success' : 'bg-warning text-dark'}`}>
                          {project.status}
                        </span>
                        <div className="btn-group" onClick={(e) => e.stopPropagation()}>
                          <button
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => openTaskModal(project)}
                            title="Add Task"
                          >
                            <i className="bi bi-plus"></i>
                          </button>
                          {project.status === 'DRAFT' && (
                            <button
                              className="btn btn-sm btn-outline-success"
                              onClick={() => handleActivateProject(project.id)}
                              title="Activate Project"
                            >
                              <i className="bi bi-check-lg"></i>
                            </button>
                          )}
                        </div>
                      </div>
                    </div>

                    {/* Tasks List */}
                    {expandedProject === project.id && (
                      <div className="ms-5 mt-2 mb-2">
                        {tasks[project.id]?.length > 0 ? (
                          <ul className="list-group list-group-flush">
                            {tasks[project.id].map((task) => (
                              <li key={task.id} className="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div className="d-flex align-items-center">
                                  <i className={`bi ${task.completed ? 'bi-check-circle-fill text-success' : 'bi-circle text-muted'} me-2`}></i>
                                  <span className={task.completed ? 'text-decoration-line-through text-muted' : ''}>
                                    {task.title}
                                  </span>
                                </div>
                                {!task.completed && (
                                  <button
                                    className="btn btn-sm btn-outline-success"
                                    onClick={() => handleCompleteTask(task.id, project.id)}
                                  >
                                    Complete
                                  </button>
                                )}
                              </li>
                            ))}
                          </ul>
                        ) : (
                          <p className="text-muted small mb-0">
                            <i className="bi bi-info-circle me-1"></i>
                            No tasks yet. Add a task to activate this project.
                          </p>
                        )}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Create Project Modal */}
      {showProjectModal && (
        <div className="modal fade show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Create New Project</h5>
                <button type="button" className="btn-close" onClick={() => setShowProjectModal(false)}></button>
              </div>
              <form onSubmit={handleCreateProject}>
                <div className="modal-body">
                  <div className="mb-3">
                    <label htmlFor="projectName" className="form-label">Project Name</label>
                    <input
                      type="text"
                      className="form-control"
                      id="projectName"
                      placeholder="Enter project name"
                      value={newProjectName}
                      onChange={(e) => setNewProjectName(e.target.value)}
                      required
                    />
                  </div>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={() => setShowProjectModal(false)}>
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Create Project
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Create Task Modal */}
      {showTaskModal && (
        <div className="modal fade show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Add Task to {selectedProject?.name}</h5>
                <button type="button" className="btn-close" onClick={() => setShowTaskModal(false)}></button>
              </div>
              <form onSubmit={handleCreateTask}>
                <div className="modal-body">
                  <div className="mb-3">
                    <label htmlFor="taskTitle" className="form-label">Task Title</label>
                    <input
                      type="text"
                      className="form-control"
                      id="taskTitle"
                      placeholder="Enter task title"
                      value={newTaskTitle}
                      onChange={(e) => setNewTaskTitle(e.target.value)}
                      required
                    />
                  </div>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={() => setShowTaskModal(false)}>
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Add Task
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;

