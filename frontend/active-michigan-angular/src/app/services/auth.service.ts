import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { AuthResponse, UserRole } from '../models/types';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/auth`;
  private readonly STORAGE_KEY = 'active-michigan-jwt';

  token = signal<string | null>(localStorage.getItem(this.STORAGE_KEY));
  currentUser = signal<AuthResponse | null>(null);

  isAuthenticated = computed(() => !!this.token());

  constructor(private http: HttpClient) {
    if (this.token()) {
      this.fetchMe().subscribe({
        error: () => this.logout()
      });
    }
  }

  fetchMe() {
    return this.http.get<AuthResponse>(`${this.API_URL}/me`, {
      headers: this.getAuthHeaders()
    }).pipe(
      tap(res => {
        this.currentUser.set({ ...res, token: this.token()! });
      })
    );
  }

  register(payload: any) {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, payload).pipe(
      tap(res => this.handleAuth(res))
    );
  }

  login(payload: any) {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, payload).pipe(
      tap(res => this.handleAuth(res))
    );
  }

  logout() {
    this.token.set(null);
    this.currentUser.set(null);
    localStorage.removeItem(this.STORAGE_KEY);
  }

  private handleAuth(res: AuthResponse) {
    this.token.set(res.token);
    this.currentUser.set(res);
    localStorage.setItem(this.STORAGE_KEY, res.token);
  }

  getAuthHeaders(withJson = true) {
    const headers: Record<string, string> = {};
    const token = this.token();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    if (withJson) {
      headers['Content-Type'] = 'application/json';
    }
    return headers;
  }
}
