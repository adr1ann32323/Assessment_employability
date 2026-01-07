-- ============================================
-- Test Data Script
-- Initial data for manual testing
-- ============================================

-- Test User 1
-- Password: password123 (BCrypt hashed)
INSERT INTO users (id, username, email, password)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'testuser',
    'test@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
);

-- Test User 2
-- Password: password123 (BCrypt hashed)
INSERT INTO users (id, username, email, password)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    'admin',
    'admin@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
);

-- Project in DRAFT status (no tasks yet)
INSERT INTO projects (id, owner_id, name, status, deleted)
VALUES (
    '33333333-3333-3333-3333-333333333333',
    '11111111-1111-1111-1111-111111111111',
    'Test Project 1',
    'DRAFT',
    false
);

-- ACTIVE project with tasks
INSERT INTO projects (id, owner_id, name, status, deleted)
VALUES (
    '44444444-4444-4444-4444-444444444444',
    '11111111-1111-1111-1111-111111111111',
    'Active Project',
    'ACTIVE',
    false
);

-- Tasks for the active project
INSERT INTO tasks (id, project_id, title, completed, deleted)
VALUES (
    '55555555-5555-5555-5555-555555555555',
    '44444444-4444-4444-4444-444444444444',
    'Implement authentication',
    true,
    false
);

INSERT INTO tasks (id, project_id, title, completed, deleted)
VALUES (
    '66666666-6666-6666-6666-666666666666',
    '44444444-4444-4444-4444-444444444444',
    'Create REST endpoints',
    false,
    false
);

-- Project for admin user
INSERT INTO projects (id, owner_id, name, status, deleted)
VALUES (
    '77777777-7777-7777-7777-777777777777',
    '22222222-2222-2222-2222-222222222222',
    'Admin Project',
    'DRAFT',
    false
);
