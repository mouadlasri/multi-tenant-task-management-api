# Multi-tenant Task Management API

Spring Boot practice project focused on tenant scoping, role-based authorization, JWT auth, nested resources, JPA relationships, Flyway migrations, and service-layer business rules.

## Stack

- Java 21
- Spring Boot 4.1
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL 17
- Flyway
- Docker Compose for local PostgreSQL
- JWT auth with stateless bearer tokens

## Domains

- `User`: authenticated identity, soft delete
- `Organization`: tenant boundary
- `Membership`: user role inside an organization
- `Project`: organization-owned project
- `Task`: project-owned task with status
- `Comment`: task comment authored by a user

## Authorization Model

Roles:

- `OWNER`
- `ADMIN`
- `MEMBER`

Rules:

- Any authenticated user can create an organization; the creator becomes `OWNER`.
- Organization update/delete requires `OWNER`.
- Membership listing/detail requires `OWNER` or `ADMIN`.
- Membership create/update/delete requires `OWNER`.
- The last owner cannot be demoted or removed.
- Project/task mutation requires `OWNER` or `ADMIN`.
- Project/task/comment reads require active organization membership.
- Comment creation requires active organization membership.
- Comment deletion requires `OWNER` or `ADMIN`.

## Local Setup

Start PostgreSQL:

```bash
docker compose up -d db
```

Database settings:

- container: `multi-tenant`
- host port: `5435`
- database: `mouad_multi_tenant_task_management_db`
- user: `mouad`

Run tests with the local profile:

```bash
./mvnw test -Dspring.profiles.active=local
```

Connect to PostgreSQL:

```bash
docker exec -it multi-tenant psql -U mouad -d mouad_multi_tenant_task_management_db
```

## Main API Flow

Register:

```http
POST /api/v1/auth/register
```

Login:

```http
POST /api/v1/auth/login
```

Use the returned JWT as:

```http
Authorization: Bearer <token>
```

Create organization:

```http
POST /api/v1/organizations
```

Create project:

```http
POST /api/v1/organizations/{organizationId}/projects
```

Create task:

```http
POST /api/v1/organizations/{organizationId}/projects/{projectId}/tasks
```

Create comment:

```http
POST /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}/comments
```

## Endpoint Groups

Organizations:

- `GET /api/v1/organizations`
- `GET /api/v1/organizations/{organizationId}`
- `POST /api/v1/organizations`
- `PATCH /api/v1/organizations/{organizationId}`
- `DELETE /api/v1/organizations/{organizationId}`

Memberships:

- `GET /api/v1/organizations/{organizationId}/members`
- `GET /api/v1/organizations/{organizationId}/members/{membershipId}`
- `POST /api/v1/organizations/{organizationId}/members`
- `PATCH /api/v1/organizations/{organizationId}/members/{membershipId}`
- `DELETE /api/v1/organizations/{organizationId}/members/{membershipId}`

Projects:

- `GET /api/v1/organizations/{organizationId}/projects`
- `GET /api/v1/organizations/{organizationId}/projects/{projectId}`
- `POST /api/v1/organizations/{organizationId}/projects`
- `PATCH /api/v1/organizations/{organizationId}/projects/{projectId}`
- `DELETE /api/v1/organizations/{organizationId}/projects/{projectId}`

Tasks:

- `GET /api/v1/organizations/{organizationId}/projects/{projectId}/tasks`
- `GET /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}`
- `POST /api/v1/organizations/{organizationId}/projects/{projectId}/tasks`
- `PATCH /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}`
- `DELETE /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}`

Comments:

- `GET /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}/comments`
- `GET /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}/comments/{commentId}`
- `POST /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}/comments`
- `DELETE /api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}/comments/{commentId}`
