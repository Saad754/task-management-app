import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest, User } from '../models/auth';
import { environment } from '../environments/environment';

@Service()
export class Auth {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/auth`;
  private tokenKey = 'auth_token';

  saveToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }
  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, request);
  }
  register(request: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/register`, request);
  }
}
