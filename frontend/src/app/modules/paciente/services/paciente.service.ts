import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';
import { Paciente } from '../models/paciente.model';

/**
 * Servicio para operaciones de perfil del paciente.
 * GET /api/pacientes/perfil
 * PUT /api/pacientes/perfil
 */
@Injectable({ providedIn: 'root' })
export class PacienteService {
  private readonly url = `${environment.apiUrl}/pacientes`;

  constructor(private http: HttpClient) { }

  getPerfil(): Observable<Paciente> {
    return this.http.get<ApiResponse<Paciente>>(`${this.url}/perfil`)
      .pipe(map(r => r.datos));
  }

  actualizarPerfil(data: Partial<Paciente>): Observable<Paciente> {
    return this.http.put<ApiResponse<Paciente>>(`${this.url}/perfil`, data)
      .pipe(map(r => r.datos));
  }
}
