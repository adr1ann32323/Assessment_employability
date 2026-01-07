# Project and Task Management System

A full-stack web application for managing projects and tasks, built with **Java Spring Boot** backend and **React** frontend, implementing **Clean Architecture** with **Hexagonal (Ports & Adapters)** pattern.

## Project Overview

This system allows users to:
- Register and authenticate using JWT tokens
- Create and manage projects with DRAFT and ACTIVE statuses
- Create tasks within projects
- Activate projects (requires at least one task)
- Complete tasks with audit logging
- Track all actions through an audit system

## Technology Stack

### Backend
- Java 21
- Spring Boot 3
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL 16
- Flyway (Database migrations)
- Swagger/OpenAPI (API Documentation)
- JUnit 5 & Mockito (Testing)

### Frontend
- React 18
- React Router DOM
- Axios
- Bootstrap 5 (via CDN)
- Vite

### Infrastructure
- Docker & Docker Compose
- PostgreSQL 16 Alpine

## Project Structure

```
Assessment_employability/
├── backend/
│   └── projects/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/Assessment_employability/projects/
│       │   │   │   ├── domain/           # Business logic (pure Java)
│       │   │   │   ├── application/      # Use cases
│       │   │   │   └── infrastructure/   # Adapters (Web, DB, Security)
│       │   │   └── resources/
│       │   │       └── db/migration/     # Flyway SQL scripts
│       │   └── test/                     # Unit tests
│       ├── Dockerfile
│       ├── docker-compose.yml
│       └── pom.xml
└── frontend/
    ├── src/
    │   ├── context/          # React Context (Auth)
    │   ├── pages/            # React Pages
    │   └── services/         # API services
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## Getting Started

### Prerequisites

- Docker and Docker Compose (for containerized deployment)
- Java 21+ (for local backend development)
- Node.js 18+ and npm (for local frontend development)
- Maven 3.8+ (for local backend development)

---

## Option 1: Running with Docker Compose (Recommended)

This method starts both the backend and PostgreSQL database in containers.

```bash
# Navigate to backend folder
cd backend/projects

# Start all services
docker compose up --build

# The API will be available at http://localhost:8080
```

To stop the services:
```bash
docker compose down
```

To stop and remove all data:
```bash
docker compose down -v
```

---

## Option 2: Running Locally (Development)

### Step 1: Start PostgreSQL Database

You can use Docker to run only the database:

```bash
docker run -d --name projects-db \
  -e POSTGRES_DB=projects \
  -e POSTGRES_USER=projects \
  -e POSTGRES_PASSWORD=projects321 \
  -p 5432:5432 \
  postgres:16-alpine
```

Or start the database from docker-compose:

```bash
cd backend/projects
docker compose up postgres -d
```

### Step 2: Start Backend

```bash
cd backend/projects

# Run the application
./mvnw spring-boot:run
```

The backend will be available at:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

### Step 3: Start Frontend

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev
```

The frontend will be available at: http://localhost:5173

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Projects
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/projects` | Yes | Create a new project |
| GET | `/api/projects` | Yes | Get user's projects |
| PATCH | `/api/projects/{id}/activate` | Yes | Activate a project |

### Tasks
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/projects/{projectId}/tasks` | Yes | Create a task |
| GET | `/api/projects/{projectId}/tasks` | No | Get project tasks |
| PATCH | `/api/tasks/{id}/complete` | Yes | Complete a task |

## Business Rules

1. A project can only be activated if it has at least one task
2. Only the project owner can modify their projects and tasks
3. A completed task cannot be modified again
4. All deletions are logical (soft delete)
5. Project activation and task completion generate audit logs
6. Project activation and task completion trigger notifications

## Test Credentials

| Username | Password |
|----------|----------|
| testuser | password123 |
| admin | password123 |

## Running Tests

```bash
cd backend/projects

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ActivateProjectServiceTest
```

## Database Schema

```sql
-- Users table
users (id UUID, username, email, password, created_at, updated_at)

-- Projects table
projects (id UUID, owner_id, name, status, deleted, created_at, updated_at)

-- Tasks table
tasks (id UUID, project_id, title, completed, deleted, created_at, updated_at)

-- Audit logs
audit_logs (id UUID, action, entity_id, user_id, created_at)
```

## Architecture Highlights

- **Hexagonal Architecture**: The domain layer is completely independent of frameworks
- **Ports and Adapters**: Clear separation between business logic and infrastructure
- **JWT Authentication**: Stateless authentication for REST API
- **Soft Delete**: Data integrity through logical deletion
- **Audit Logging**: All critical actions are logged for traceability

---

## Author

**Adrian Alesis Arboleda**

- Email: adr1ann32323@gmail.com
- GitHub: [adr1ann32323](https://github.com/adr1ann32323)

