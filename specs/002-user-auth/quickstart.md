# Quickstart: 002-user-auth

**Feature**: User Authentication (Email & Password) | **Branch**: 002-user-auth

## Prerequisites

- Java 21+
- Node.js 20+ (for frontend)
- MongoDB 6+ (local or Docker)
- Maven 3.9+

## Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

Default: `http://localhost:8080`

Required env (or `application.yml`):
- `MONGODB_URI`: MongoDB connection string (e.g. `mongodb://localhost:27017/aiml_fintech`)

## Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

Default: `http://localhost:5173` (or 3000)

Configure API base URL via `VITE_API_URL` or `.env` (e.g. `http://localhost:8080`).

## MongoDB

```bash
# Docker
docker run -d -p 27017:27017 mongo:7
```

Or use a cloud instance (MongoDB Atlas); set `MONGODB_URI` accordingly.

## Verify Auth Flow

1. **Sign up**: POST `/api/v1/auth/sign-up` with `{"email":"test@example.com","password":"Test1234"}`
2. **Sign in**: POST `/api/v1/auth/sign-in` with same credentials
3. **Current user**: GET `/api/v1/auth/me` (with session cookie)
4. **Sign out**: POST `/api/v1/auth/sign-out`

Or use the React UI: Sign Up → Sign In → Sign Out.

## Contract Validation

```bash
# From repo root, run contract tests (when implemented)
cd backend && mvn test -Dtest=*Contract*
```

## Notes

- Session cookie: `SESSION` (or `JSESSIONID`); HttpOnly, SameSite=Strict
- CORS: Backend allows `http://localhost:5173` (and 3000) in dev with credentials
