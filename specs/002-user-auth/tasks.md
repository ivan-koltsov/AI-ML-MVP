# Tasks: User Authentication (Email & Password)

**Input**: Design documents from `/specs/002-user-auth/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Contract test included per plan (Constitution: contract tests for API). No unit/integration tests requested in spec.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Web app**: `backend/src/`, `frontend/src/` at repository root

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create backend project structure with backend/pom.xml and backend/src/main/java/com/aiml/fintech/
- [ ] T002 Initialize backend with Spring Boot 3.x, Spring Security, Spring Data MongoDB in backend/pom.xml
- [ ] T003 [P] Create frontend project with Vite, React 18, TypeScript in frontend/
- [ ] T004 [P] Configure backend application.yml with MongoDB URI and server port in backend/src/main/resources/application.yml
- [ ] T005 [P] Configure frontend .env with VITE_API_URL placeholder for API base URL

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core auth infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T006 Create User document model in backend/src/main/java/com/aiml/fintech/auth/User.java
- [ ] T007 Create UserRepository extending MongoRepository in backend/src/main/java/com/aiml/fintech/auth/UserRepository.java
- [ ] T008 Implement SecurityConfig with BCryptPasswordEncoder and session management in backend/src/main/java/com/aiml/fintech/config/SecurityConfig.java
- [ ] T009 Implement AuthService with signIn, signUp, signOut methods in backend/src/main/java/com/aiml/fintech/auth/AuthService.java
- [ ] T010 Implement AuthController with POST /auth/sign-in, /auth/sign-up, /auth/sign-out and GET /auth/me in backend/src/main/java/com/aiml/fintech/auth/AuthController.java
- [ ] T011 [P] Add structured logging for auth events in backend/src/main/java/com/aiml/fintech/auth/AuthService.java
- [ ] T012 [P] Configure CORS for dev frontend origin in backend/src/main/java/com/aiml/fintech/config/WebConfig.java or SecurityConfig.java

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Sign In (Priority: P1) 🎯 MVP

**Goal**: User can sign in with email and password and access the application

**Independent Test**: Enter valid email and password, receive session cookie, access protected area; enter invalid credentials, see error

### Implementation for User Story 1

- [ ] T013 [US1] Create authApi.ts with signIn and getCurrentUser in frontend/src/services/authApi.ts
- [ ] T014 [US1] Create SignInPage with email and password form in frontend/src/pages/SignInPage.tsx
- [ ] T015 [US1] Add sign-in route and redirect to home on success in frontend
- [ ] T016 [US1] Add protected route guard redirecting unauthenticated users to sign-in in frontend

**Checkpoint**: User Story 1 fully functional - sign-in works end-to-end

---

## Phase 4: User Story 2 - Sign Up (Priority: P2)

**Goal**: New user can create account with email and password and be signed in

**Independent Test**: Submit valid email and password, account created, user signed in; duplicate email rejected with error

### Implementation for User Story 2

- [ ] T017 [P] [US2] Add signUp method to frontend/src/services/authApi.ts
- [ ] T018 [US2] Create SignUpPage with email and password validation in frontend/src/pages/SignUpPage.tsx
- [ ] T019 [US2] Add sign-up route and redirect to home after successful sign-up in frontend

**Checkpoint**: User Stories 1 and 2 both work - sign-up and sign-in independently testable

---

## Phase 5: User Story 3 - Sign Out (Priority: P3)

**Goal**: Signed-in user can sign out; session ends and protected content is inaccessible

**Independent Test**: Sign in, click sign out, verify session cleared and redirect to sign-in; protected routes redirect

### Implementation for User Story 3

- [ ] T020 [P] [US3] Add signOut method to frontend/src/services/authApi.ts
- [ ] T021 [US3] Create SignOut button and wire to signOut + redirect in frontend
- [ ] T022 [US3] Add sign-out to app shell/navigation and ensure protected routes redirect when logged out

**Checkpoint**: All user stories independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T023 [P] Contract test for auth API against specs/002-user-auth/contracts/auth-api.yaml in backend/src/test/java/com/aiml/fintech/contract/AuthApiContractTest.java
- [ ] T024 Run quickstart.md validation (backend + frontend + MongoDB)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3–5)**: All depend on Foundational phase completion
  - US1, US2, US3 can proceed sequentially (P1 → P2 → P3) or in parallel if staffed
- **Polish (Phase 6)**: Depends on all user stories complete

### User Story Dependencies

- **User Story 1 (P1)**: After Foundational - no dependencies on US2/US3
- **User Story 2 (P2)**: After Foundational - no dependencies on US1/US3 (can create user via API)
- **User Story 3 (P3)**: After Foundational - needs signed-in user to test; functionally independent

### Within Each Phase

- Phase 2: User → UserRepository → AuthService → AuthController (T006–T010 sequential); T011, T012 parallel
- Phase 3: authApi → SignInPage → routes → guard
- Phase 4: signUp in authApi → SignUpPage → route
- Phase 5: signOut in authApi → SignOut button → integration

### Parallel Opportunities

- T003, T004, T005 within Setup
- T011, T012 within Foundational
- T017, T020 (authApi additions) can run parallel to page work in their phase
- T023 in Polish

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1 (Sign In)
4. **STOP and VALIDATE**: Sign-in works end-to-end
5. Deploy/demo if ready

### Incremental Delivery

1. Setup + Foundational → foundation ready
2. Add User Story 1 → sign-in MVP → deploy
3. Add User Story 2 → sign-up → deploy
4. Add User Story 3 → sign-out → deploy
5. Polish (contract test, quickstart validation)

### Parallel Team Strategy

- Team completes Setup + Foundational together
- Once Foundational done: one developer can own US1, another US2, another US3
- Stories integrate at shared authApi and route layer

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to user story for traceability
- Each user story independently completable and testable
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
