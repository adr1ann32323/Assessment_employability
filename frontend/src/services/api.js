import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 responses
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth endpoints
export const authService = {
  login: (username, password) => api.post('/auth/login', { username, password }),
  register: (username, email, password) => api.post('/auth/register', { username, email, password }),
};

// Project endpoints
export const projectService = {
  getAll: () => api.get('/projects'),
  create: (name) => api.post('/projects', { name }),
  activate: (id) => api.patch(`/projects/${id}/activate`),
};

// Task endpoints
export const taskService = {
  getByProject: (projectId) => api.get(`/projects/${projectId}/tasks`),
  create: (projectId, title) => api.post(`/projects/${projectId}/tasks`, { title }),
  complete: (id) => api.patch(`/tasks/${id}/complete`),
};

export default api;

