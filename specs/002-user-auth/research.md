# Research: User Authentication (Email & Password)

**Feature**: 002-user-auth | **Date**: 2025-02-18

## 1. Session vs Token (JWT) Authentication

**Decision**: Session-based authentication with HTTP-only cookies

**Rationale**:
- Same-origin React app talking to Spring backend; session cookies are simpler and avoid XSS token theft
- Spring Security provides built-in session management and CSRF
- Session invalidation (sign-out) is straightforward; no token blacklist needed

**Alternatives considered**:
- JWT in localStorage: Rejected—vulnerable to XSS; requires refresh token flow for secure rotation
- JWT in HTTP-only cookie: Viable but adds token expiry/refresh complexity; session cookies achieve same outcome with less code for MVP

---

## 2. Password Hashing

**Decision**: bcrypt via Spring Security's `BCryptPasswordEncoder`

**Rationale**:
- Industry standard; Spring Security integrates natively
- Configurable work factor for future hardening
- No need to choose salt—bcrypt handles it

**Alternatives considered**:
- Argon2: Stronger but less ecosystem support in Spring
- scrypt: Similar trade-off; bcrypt sufficient for FinTech MVP

---

## 3. Session Storage

**Decision**: In-memory session store (Spring default)

**Rationale**:
- YAGNI: single-instance deployment initially
- No additional infrastructure (Redis, etc.)
- Can migrate to Redis/session DB when scaling horizontally

**Alternatives considered**:
- Redis: Adds ops complexity; defer until multi-instance
- MongoDB session store: Possible but not first-class Spring support; in-memory sufficient for MVP

---

## 4. CORS and Cookie Configuration

**Decision**: Same-origin for MVP; explicit CORS only if frontend and backend differ by port in dev

**Rationale**:
- Dev: frontend (e.g. 3000) and backend (e.g. 8080) need `credentials: include` and backend `AllowCredentials=true` with explicit origin
- Production: Same domain via reverse proxy; no CORS needed

**Alternatives considered**:
- Wildcard CORS: Rejected—breaks credentials; must specify origin

---

## 5. Email Validation

**Decision**: RFC 5322–style validation on backend; frontend uses HTML5 `type="email"` and basic regex for UX

**Rationale**:
- Backend is source of truth; reject invalid emails at API boundary
- Spring Validation (`@Email`) or Hibernate Validator covers common cases

---

## 6. Password Requirements

**Decision**: Minimum 8 characters; at least one letter and one digit

**Rationale**:
- Spec states "minimum 8 characters; mix of letters and numbers"
- Regex validation on both frontend (immediate feedback) and backend (security)

---

## 7. Rate Limiting / Lockout

**Decision**: Defer to later phase; log failed attempts for audit

**Rationale**:
- Spec allows "excessive failed attempts may trigger rate limiting"—not mandatory for MVP
- FinTech Data Integrity: logging satisfies traceability; add rate limiting when scaling
