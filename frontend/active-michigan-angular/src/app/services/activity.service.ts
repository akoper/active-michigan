import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Activity, ActivityPage } from '../models/types';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private readonly API_URL = `${environment.apiUrl}/activities`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getActivities(params: any = {}) {
    let httpParams = new HttpParams();
    Object.keys(params).forEach(key => {
      if (params[key] !== undefined && params[key] !== null && params[key] !== '') {
        httpParams = httpParams.set(key, params[key]);
      }
    });
    return this.http.get<ActivityPage>(this.API_URL, { params: httpParams });
  }

  getActivity(id: number) {
    return this.http.get<Activity>(`${this.API_URL}/${id}`);
  }

  createActivity(payload: any) {
    return this.http.post<Activity>(this.API_URL, payload, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateActivity(id: number, payload: any) {
    return this.http.put<Activity>(`${this.API_URL}/${id}`, payload, {
      headers: this.authService.getAuthHeaders()
    });
  }

  deleteActivity(id: number) {
    return this.http.delete(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders(false)
    });
  }
}
