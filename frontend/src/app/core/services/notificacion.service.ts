import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface NotificacionResponse {
  idNotificacion: number;
  tipo: string;
  mensaje: string;
  fecha: string;
  leido: boolean;
}

export interface ApiResponse<T> {
  exito: boolean;
  mensaje: string;
  datos: T;
}

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private apiUrl = `${environment.apiUrl}/notificaciones`;

  constructor(private http: HttpClient) {}

  obtenerMisNotificaciones(): Observable<ApiResponse<NotificacionResponse[]>> {
    return this.http.get<ApiResponse<NotificacionResponse[]>>(`${this.apiUrl}/mis-notificaciones`);
  }

  marcarComoLeidas(): Observable<ApiResponse<string>> {
    return this.http.put<ApiResponse<string>>(`${this.apiUrl}/marcar-leidas`, {});
  }
}
