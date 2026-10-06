import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response.model';

export interface CitaResumenItem {
  idCita: number;
  nombrePaciente: string;
  dniPaciente: string;
  horaInicio: string;
  horaFin: string;
  nombreMedico: string;
  nombreEspecialidad: string;
  estado: string;
}

export interface DashboardAdminData {
  totalCitas: number;
  totalPacientes: number;
  totalMedicos: number;
  citasHoy: number;
  tasaAusentismo: number;
  citasPorEstado: { estado: string; cantidad: number }[];
  citasDelDia?: CitaResumenItem[];
}

export interface MedicoRequest {
  nombres: string; apellidos: string; correo: string;
  usuario: string; contrasena?: string; idEspecialidad: number;
}

export interface TurnoRequest {
  idMedico: number; fecha: string;
  horaInicio: string; horaFin: string;
}

export interface AusentismoMedicoItem {
  nombreMedico: string;
  especialidad: string;
  totalCitas: number;
  noAsistio: number;
  tasa: number;
}

export interface ConfiguracionRequest {
  nombreClinica?: string; correoContacto?: string;
  telefonoContacto?: string; direccion?: string;
  horarioAperturaGeneral?: string; horarioCierreGeneral?: string;
  duracionTurnoMinutos?: number;
  diasAnticipacionReserva?: number;
  diasMinimosCancelacion?: number;
}

export interface TurnoDisponibleAdmin {
  idTurno: number;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  idMedico: number;
  nombreMedico: string;
  idEspecialidad: number;
  especialidad: string;
}

export interface HorarioMedicoItem {
  idHorario: number;
  idMedico: number;
  nombreMedico: string;
  diaSemana: number;
  diaNombre: string;
  horaInicio: string;
  horaFin: string;
  duracionTurnoMin: number;
  activo: boolean;
}

export interface HorarioMedicoRequest {
  idMedico: number;
  diaSemana: number;
  horaInicio: string;
  horaFin: string;
  duracionTurnoMin: number;
}

export interface GenerarTurnosRequest {
  idMedico: number;
  fechaDesde: string;
  fechaHasta: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getDashboard(): Observable<DashboardAdminData> {
    return this.http.get<ApiResponse<DashboardAdminData>>(`${this.baseUrl}/dashboard/resumen`)
      .pipe(map(r => r.datos));
  }

  getMedicos(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${this.baseUrl}/medicos`)
      .pipe(map(r => r.datos || []));
  }

  crearMedico(data: MedicoRequest): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/medicos`, data)
      .pipe(map(r => r.datos));
  }

  actualizarMedico(id: number, data: MedicoRequest): Observable<any> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/medicos/${id}`, data)
      .pipe(map(r => r.datos));
  }

  desactivarMedico(id: number): Observable<string> {
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/medicos/${id}`)
      .pipe(map(r => r.datos));
  }

  getTurnos(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${this.baseUrl}/turnos`)
      .pipe(map(r => r.datos || []));
  }

  crearTurno(data: TurnoRequest): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/turnos`, data)
      .pipe(map(r => r.datos));
  }

  eliminarTurno(id: number): Observable<string> {
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/turnos/${id}`)
      .pipe(map(r => r.datos));
  }

  getPacientes(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${this.baseUrl}/pacientes`)
      .pipe(map(r => r.datos || []));
  }

  desactivarPaciente(id: number): Observable<string> {
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/pacientes/${id}`)
      .pipe(map(r => r.datos));
  }

  getReporteCitas(desde?: string, hasta?: string): Observable<any[]> {
    let params = new HttpParams();
    if (desde) params = params.set('desde', desde);
    if (hasta) params = params.set('hasta', hasta);
    return this.http.get<ApiResponse<any[]>>(`${this.baseUrl}/reportes/citas`, { params })
      .pipe(map(r => r.datos || []));
  }

  getReporteAusentismo(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${this.baseUrl}/reportes/ausentismo`)
      .pipe(map(r => r.datos || []));
  }

  getReporteAusentismoPorMedico(desde?: string, hasta?: string): Observable<AusentismoMedicoItem[]> {
    let params = new HttpParams();
    if (desde) params = params.set('desde', desde);
    if (hasta) params = params.set('hasta', hasta);
    return this.http.get<ApiResponse<AusentismoMedicoItem[]>>(`${this.baseUrl}/reportes/ausentismo-por-medico`, { params })
      .pipe(map(r => r.datos || []));
  }

  getConfiguracion(): Observable<any> {
    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/configuracion`)
      .pipe(map(r => r.datos));
  }

  actualizarConfiguracion(data: ConfiguracionRequest): Observable<any> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/configuracion`, data)
      .pipe(map(r => r.datos));
  }

  getEspecialidades(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${environment.apiUrl}/especialidades`)
      .pipe(map(r => r.datos || []));
  }

  crearEspecialidad(nombre: string, descripcion?: string): Observable<any> {
    let params = new HttpParams().set('nombre', nombre);
    if (descripcion) {
      params = params.set('descripcion', descripcion);
    }
    return this.http.post<ApiResponse<any>>(`${environment.apiUrl}/especialidades`, {}, { params })
      .pipe(map(r => r.datos));
  }

  getMedicosPorEspecialidad(idEspecialidad: number): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${environment.apiUrl}/medicos/especialidad/${idEspecialidad}`)
      .pipe(map(r => r.datos || []));
  }

  getTurnosDisponibles(medicoId: number, fecha: string): Observable<TurnoDisponibleAdmin[]> {
    let params = new HttpParams().set('medicoId', medicoId.toString()).set('fecha', fecha);
    return this.http.get<ApiResponse<TurnoDisponibleAdmin[]>>(`${environment.apiUrl}/turnos/disponibles`, { params })
      .pipe(map(r => r.datos || []));
  }

  reservarCitaParaPaciente(idPaciente: number, idTurno: number): Observable<string> {
    const params = new HttpParams().set('idPaciente', idPaciente.toString());
    return this.http.post<ApiResponse<string>>(`${environment.apiUrl}/citas/reservar`, { idTurno }, { params })
      .pipe(map(r => r.datos));
  }

  getHorarios(medicoId: number): Observable<HorarioMedicoItem[]> {
    const params = new HttpParams().set('medicoId', medicoId.toString());
    return this.http.get<ApiResponse<HorarioMedicoItem[]>>(`${environment.apiUrl}/horarios`, { params })
      .pipe(map(r => r.datos || []));
  }

  crearHorario(data: HorarioMedicoRequest): Observable<HorarioMedicoItem> {
    return this.http.post<ApiResponse<HorarioMedicoItem>>(`${environment.apiUrl}/horarios`, data)
      .pipe(map(r => r.datos));
  }

  eliminarHorario(id: number): Observable<string> {
    return this.http.delete<ApiResponse<string>>(`${environment.apiUrl}/horarios/${id}`)
      .pipe(map(r => r.datos));
  }

  generarTurnosEnLote(data: GenerarTurnosRequest): Observable<string> {
    return this.http.post<ApiResponse<string>>(`${environment.apiUrl}/horarios/generar-turnos`, data)
      .pipe(map(r => r.mensaje));
  }
}
