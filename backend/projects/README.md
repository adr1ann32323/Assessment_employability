# Project Management API

A REST API for managing projects and tasks built with **Java 21**, **Spring Boot 3**, and **Hexagonal Architecture (Clean Architecture)**.

## Architecture

This project implements **Clean Architecture** with **Hexagonal/Ports & Adapters** pattern:

```
src/main/java/com/Assessment_employability/projects/
├── ProjectsApplication.java
├── domain/
│   ├── model/
│   │   ├── Project.java
│   │   ├── ProjectStatus.java
│   │   ├── Task.java
│   │   └── User.java
│   ├── exception/
│   │   ├── BusinessRuleException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── UnauthorizedException.java
│   └── port/
│       ├── in/
│       │   ├── ActivateProjectUseCase.java
│       │   ├── CompleteTaskUseCase.java
│       │   ├── CreateProjectUseCase.java
│       │   ├── CreateTaskUseCase.java
│       │   └── GetProjectsUseCase.java
│       └── out/
│           ├── AuditLogPort.java
│           ├── CurrentUserPort.java
│           ├── NotificationPort.java
│           ├── ProjectRepositoryPort.java
│           ├── TaskRepositoryPort.java
│           └── UserRepositoryPort.java
├── application/
│   └── service/
│       ├── ActivateProjectService.java
│       ├── CompleteTaskService.java
│       ├── CreateProjectService.java
│       ├── CreateTaskService.java
│       └── GetProjectsService.java
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   └── web/
    │   │       ├── AuthController.java
    │   │       ├── GlobalExceptionHandler.java
    │   │       ├── ProjectController.java
    │   │       ├── TaskController.java
    │   │       ├── dto/
    │   │       │   ├── AuthResponse.java
    │   │       │   ├── CreateProjectRequest.java
    │   │       │   ├── CreateTaskRequest.java
    │   │       │   ├── LoginRequest.java
    │   │       │   ├── ProjectResponse.java
    │   │       │   ├── RegisterRequest.java
    │   │       │   └── TaskResponse.java
    │   │       └── mapper/
    │   │           ├── ProjectDtoMapper.java
    │   │           └── TaskDtoMapper.java
    │   └── out/
    │       ├── persistence/
    │       │   ├── ProjectRepositoryAdapter.java
    │       │   ├── TaskRepositoryAdapter.java
    │       │   ├── UserRepositoryAdapter.java
    │       │   ├── entity/
    │       │   │   ├── AuditLogEntity.java
    │       │   │   ├── ProjectEntity.java
    │       │   │   ├── TaskEntity.java
    │       │   │   └── UserEntity.java
    │       │   ├── mapper/
    │       │   │   ├── ProjectMapper.java
    │       │   │   ├── TaskMapper.java
    │       │   │   └── UserMapper.java
    │       │   └── repository/
    │       │       ├── JpaAuditLogRepository.java
    │       │       ├── JpaProjectRepository.java
    │       │       ├── JpaTaskRepository.java
    │       │       └── JpaUserRepository.java
    │       ├── security/
    │       │   ├── CurrentUserAdapter.java
    │       │   ├── JwtAuthenticationEntryPoint.java
    │       │   ├── JwtAuthenticationFilter.java
    │       │   └── JwtTokenProvider.java
    │       ├── audit/
    │       │   └── AuditLogAdapter.java
    │       └── notification/
    │           └── LogNotificationAdapter.java
    └── config/
        ├── BeanConfig.java
        ├── OpenApiConfig.java
        └── SecurityConfig.java
```

## Quick Start

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven 3.8+

### Running with Docker Compose (Recommended)

```bash
# Start the application with PostgreSQL
docker compose up --build

# The API will be available at http://localhost:8080
```

### Running Locally (Development)

1. **Start PostgreSQL database:**
```bash
docker run -d --name projects-db \
  -e POSTGRES_DB=projects \
  -e POSTGRES_USER=projects \
  -e POSTGRES_PASSWORD=projects321 \
  -p 5432:5432 \
  postgres:16-alpine
```

2. **Run the application:**
```bash
./mvnw spring-boot:run
```

## API Documentation

Swagger UI is available at: **http://localhost:8080/swagger-ui.html**

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Project Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/projects` | Yes | Create a new project |
| GET | `/api/projects` | No | Get all user projects |
| PATCH | `/api/projects/{id}/activate` | Yes | Activate a project |

### Task Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/projects/{projectId}/tasks` | Yes | Create a task |
| GET | `/api/projects/{projectId}/tasks` | No | Get project tasks |
| PATCH | `/api/tasks/{id}/complete` | Yes | Complete a task |

## Test Credentials

| Username | Password | Description |
|----------|----------|-------------|
| testuser | password123 | Regular test user |
| admin | password123 | Admin test user |

## Business Rules

1. **Project Activation**: A project can only be activated if it has at least one task
2. **Ownership**: Only the project owner can modify projects and tasks
3. **Immutable Tasks**: A completed task cannot be modified
4. **Soft Delete**: All deletions are logical (soft delete)
5. **Audit**: Project activation and task completion generate audit logs
6. **Notifications**: Project activation and task completion trigger notifications

## Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ActivateProjectServiceTest
```

### Test Coverage

The following critical business rules are tested:
- `ActivateProject_WithTasks_ShouldSucceed`
- `ActivateProject_WithoutTasks_ShouldFail`
- `ActivateProject_ByNonOwner_ShouldFail`
- `CompleteTask_AlreadyCompleted_ShouldFail`
- `CompleteTask_ShouldGenerateAuditAndNotification`

## Technical Decisions

### Why PostgreSQL?
- Native UUID support with `gen_random_uuid()`
- Robust constraint checking
- Better performance for complex queries
- Industry standard for production applications

### Why Hexagonal Architecture?
- **Domain Independence**: Business logic doesn't depend on frameworks
- **Testability**: Easy to mock ports for unit testing
- **Flexibility**: Can swap implementations (e.g., change database) without affecting business logic
- **Maintainability**: Clear separation of concerns

### Why JWT?
- Stateless authentication
- Easy to scale horizontally
- Standard for modern REST APIs

## Database Schema

```sql
-- Users table
users (id, username, email, password, created_at, updated_at)

-- Projects table (with soft delete)
projects (id, owner_id, name, status, deleted, created_at, updated_at)

-- Tasks table (with soft delete)
tasks (id, project_id, title, completed, deleted, created_at, updated_at)

-- Audit logs
audit_logs (id, action, entity_id, user_id, created_at)
```

## Docker Configuration

- **Backend**: Java 21 with multi-stage build for smaller image
- **Database**: PostgreSQL 16 Alpine
- **Network**: Bridge network for service communication.


