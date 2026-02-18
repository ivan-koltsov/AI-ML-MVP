# Data Model: User Authentication

**Feature**: 002-user-auth | **Date**: 2025-02-18

## Entities

### User

Represents an authenticated identity. Stored in MongoDB `users` collection.

| Field | Type | Constraints | Notes |
|-------|------|--------------|-------|
| id | string (UUID) | required, unique | Primary key |
| email | string | required, unique, valid email format | Sign-in identifier |
| passwordHash | string | required | bcrypt hash; never exposed |
| createdAt | datetime | required | ISO 8601 |
| updatedAt | datetime | required | ISO 8601 |

**Validation rules** (from spec):
- Email: valid format, max 255 chars
- Password (before hash): min 8 chars, at least one letter, one digit

**State**: No state machine; user exists or does not.

---

### Session

Handled by Spring Security session store (in-memory for MVP). Not persisted in MongoDB.

| Concept | Implementation |
|---------|----------------|
| Session ID | Spring session cookie (JSESSIONID or custom) |
| User binding | Spring Security `SecurityContext` |
| Expiry | Configurable (e.g. 24h idle, 7d max) |
| Invalidation | Sign-out clears server-side session |

---

## Collections (MongoDB)

### users

```text
{
  "_id": "ObjectId or UUID",
  "email": "user@example.com",
  "passwordHash": "$2a$10$...",
  "createdAt": "2025-02-18T12:00:00Z",
  "updatedAt": "2025-02-18T12:00:00Z"
}
```

**Indexes**:
- `email` (unique)
- `createdAt` (optional, for audit queries)

---

## Relationships

- User → Session: One user can have multiple active sessions (different devices). Session references user ID in SecurityContext.

---

## Audit / Traceability (Constitution: FinTech Data Integrity)

- User creation: log event with `userId`, `email` (hashed or redacted per policy), `timestamp`
- Sign-in success/failure: log `email` (or hash), `timestamp`, `outcome`
- Sign-out: log `userId`, `timestamp`
