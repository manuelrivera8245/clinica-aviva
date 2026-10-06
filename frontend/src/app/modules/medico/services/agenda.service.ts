import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';

export interface CitaResponse {
  idCita: number;
  fechaReserva: string;
  estado: string;
  idPaciente: number;
  nombrePaciente: string;
  dniPaciente: string;
  telefonoPaciente: string;
  idTurno: number;
  fechaCita: string;
  horaInicio: string;
  horaFin: string;
  idMedico: number;
  nombreMedico: string;
  idEspecialidad: number;
  nombreEspecialidad: string;
}

export interface ActualizarEstadoRequest {
  idCita: number;
  estado: 'Atendida' | 'No Asistio';
}

// KPIs del dashboard medico
export interface DashboardMedicoData {
  citasHoy: number;
  programadas: number;
  atendidas: number;
  noAsistio: number;
  tasaAusentismo: number;
  citasDelDia: CitaResponse[];
}

// Servicio de agenda del médico
@Injectable({ providedIn: 'root' })
export class AgendaService {
  private readonly citasUrl = `${environment.apiUrl}/citas`;
  private readonly dashboardUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) { }

  getAgendaDelDia(): Observable<CitaResponse[]> {
    return this.http.get<ApiResponse<CitaResponse[]>>(`${this.citasUrl}/agenda-dia`)
      .pipe(map(r => r.datos || []));
  }

  getHistorialMedico(): Observable<CitaResponse[]> {
    // Obtener historial del medico
    return this.http.get<ApiResponse<CitaResponse[]>>(`${this.citasUrl}/historial-medico`)
      .pipe(map(r => r.datos || []));
  }

  actualizarEstado(request: ActualizarEstadoRequest): Observable<string> {
    return this.http.put<ApiResponse<string>>(`${this.citasUrl}/actualizar-estado`, request)
      .pipe(map(r => r.datos));
  }

  getDashboardMedico(): Observable<DashboardMedicoData> {
    return this.http.get<ApiResponse<DashboardMedicoData>>(`${this.dashboardUrl}/medico`)
      .pipe(map(r => r.datos));
  }
}
