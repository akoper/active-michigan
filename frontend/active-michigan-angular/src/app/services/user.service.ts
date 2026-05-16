import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/types';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getUsers() {
    return this.http.get<User[]>(this.API_URL, {
      headers: this.authService.getAuthHeaders(false)
    });
  }

  createUser(payload: any) {
    return this.http.post<User>(this.API_URL, payload, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateUser(id: number, payload: any) {
    return this.http.put<User>(`${this.API_URL}/${id}`, payload, {
      headers: this.authService.getAuthHeaders()
    });
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders(false)
    });
  }
}
