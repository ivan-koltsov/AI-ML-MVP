const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080';
const AUTH_PREFIX = `${API_BASE}/api/v1/auth`;

export interface UserResponse {
  id: string;
  email: string;
}

export async function signIn(email: string, password: string): Promise<UserResponse> {
  const res = await fetch(`${AUTH_PREFIX}/sign-in`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ email, password }),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || 'Sign in failed');
  }
  return res.json();
}

export async function signUp(email: string, password: string): Promise<UserResponse> {
  const res = await fetch(`${AUTH_PREFIX}/sign-up`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ email, password }),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || 'Sign up failed');
  }
  return res.json();
}

export async function signOut(): Promise<void> {
  await fetch(`${AUTH_PREFIX}/sign-out`, {
    method: 'POST',
    credentials: 'include',
  });
}

export async function getCurrentUser(): Promise<UserResponse | null> {
  const res = await fetch(`${AUTH_PREFIX}/me`, { credentials: 'include' });
  if (res.status === 401) return null;
  if (!res.ok) throw new Error('Failed to get current user');
  return res.json();
}
