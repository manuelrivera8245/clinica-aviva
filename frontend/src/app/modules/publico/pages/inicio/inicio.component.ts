import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../../../environments/environment';
import { ApiResponse } from '../../../../core/models/api-response.model';
import { AuthService } from '../../../../core/services/auth.service';

interface Especialidad { idEspecialidad: number; nombre: string; descripcion: string; }

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  especialidades: Especialidad[] = [];
  isAuthenticated = false;

  readonly estadisticas = [
    { valor: '+500', etiqueta: 'Pacientes Atendidos', icono: 'fa-users' },
    { valor: '+30', etiqueta: 'Médicos Especialistas', icono: 'fa-user-md' },
    { valor: '10', etiqueta: 'Especialidades', icono: 'fa-stethoscope' },
    { valor: '24/7', etiqueta: 'Disponibilidad', icono: 'fa-clock' }
  ];

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isAuthenticated();
    this.http.get<ApiResponse<Especialidad[]>>(`${environment.apiUrl}/especialidades`)
      .subscribe({ next: res => this.especialidades = (res.datos || []).slice(0, 6) });
  }

  irAReservar(): void {
    if (this.isAuthenticated) {
      this.router.navigate(['/paciente/reservar']);
    } else {
      this.router.navigate(['/auth/registro']);
    }
  }
}
