import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';
import { CitaResponse, TurnoDisponible } from '../models/paciente.model';

export interface ReservaCitaRequest {
  idTurno: number;
}

export interface CancelacionCitaRequest {
  idCita: number;
}

/**
 * Servicio de citas para el módulo paciente.
 * Endpoints:
 *   GET  /api/citas/mis-citas
 *   GET  /api/citas/historial
 *   POST /api/citas/reservar
 *   POST /api/citas/cancelar
 *   GET  /api/turnos/disponibles
 *   GET  /api/especialidades
 *   GET  /api/medicos/especialidad/{id}
 */
@Injectable({ providedIn: 'root' })
export class CitaPacienteService {
  private readonly citasUrl = `${environment.apiUrl}/citas`;
  private readonly turnosUrl = `${environment.apiUrl}/turnos`;
  private readonly especUrl = `${environment.apiUrl}/especialidades`;
  private readonly medicosUrl = `${environment.apiUrl}/medicos`;

  constructor(private http: HttpClient) { }

  getMisCitas(): Observable<CitaResponse[]> {
    return this.http.get<ApiResponse<CitaResponse[]>>(`${this.citasUrl}/mis-citas`)
      .pipe(map(r => r.datos || []));
  }

  getHistorial(): Observable<CitaResponse[]> {
    return this.http.get<ApiResponse<CitaResponse[]>>(`${this.citasUrl}/historial`)
      .pipe(map(r => r.datos || []));
  }

  reservar(request: ReservaCitaRequest): Observable<string> {
    return this.http.post<ApiResponse<string>>(`${this.citasUrl}/reservar`, request)
      .pipe(map(r => r.datos));
  }

  cancelar(request: CancelacionCitaRequest): Observable<string> {
    return this.http.post<ApiResponse<string>>(`${this.citasUrl}/cancelar`, request)
      .pipe(map(r => r.datos));
  }

  getTurnosDisponibles(medicoId?: number, fecha?: string): Observable<TurnoDisponible[]> {
    let params = new HttpParams();
    if (medicoId) params = params.set('medicoId', medicoId.toString());
    if (fecha) params = params.set('fecha', fecha);
    return this.http.get<ApiResponse<TurnoDisponible[]>>(`${this.turnosUrl}/disponibles`, { params })
      .pipe(map(r => r.datos || []));
  }

  getEspecialidades(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(this.especUrl)
      .pipe(map(r => r.datos || []));
  }

  getMedicosPorEspecialidad(idEspecialidad: number): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${this.medicosUrl}/especialidad/${idEspecialidad}`)
      .pipe(map(r => r.datos || []));
  }

  descargarComprobante(idCita: number): Observable<Blob> {
    return this.http.get(`${this.citasUrl}/${idCita}/comprobante`, { responseType: 'blob' });
  }
}
