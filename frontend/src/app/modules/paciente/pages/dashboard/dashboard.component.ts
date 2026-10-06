import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { CitaPacienteService } from '../../services/cita-paciente.service';
import { CitaResponse } from '../../models/paciente.model';
import { UsuarioSesion } from '../../../../core/models/usuario.model';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-dashboard-paciente',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  usuario: UsuarioSesion | null = null;
  citasProgramadas: CitaResponse[] = [];
  cargando = true;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-th-large',       ruta: '/paciente/dashboard' },
    { label: 'Reservar Cita', icon: 'fa-calendar-plus',  ruta: '/paciente/reservar'  },
    { label: 'Mis Citas',     icon: 'fa-calendar-check', ruta: '/paciente/mis-citas' },
    { label: 'Historial',     icon: 'fa-history',        ruta: '/paciente/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user',           ruta: '/paciente/perfil'    }
  ];

  constructor(
    private authService: AuthService,
    private citaService: CitaPacienteService
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.getUsuarioActual();
    this.citaService.getMisCitas().subscribe({
      next: citas => { this.citasProgramadas = citas.slice(0, 3); this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get proximaCita(): CitaResponse | null {
    return this.citasProgramadas.length > 0 ? this.citasProgramadas[0] : null;
  }
}
