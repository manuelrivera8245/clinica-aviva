import { Component, OnInit } from '@angular/core';
import { AgendaService, DashboardMedicoData } from '../../services/agenda.service';
import { MedicoService } from '../../services/medico.service';
import { AuthService } from '../../../../core/services/auth.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-dashboard-medico', templateUrl: './dashboard.component.html', styleUrls: ['./dashboard.component.css'] })
export class DashboardMedicoComponent implements OnInit {
  dashboardData: DashboardMedicoData | null = null;
  cargando = true;
  nombreMedico = '';

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',       ruta: '/medico/dashboard' },
    { label: 'Agenda de Hoy', icon: 'fa-calendar-day',    ruta: '/medico/agenda'    },
    { label: 'Historial',     icon: 'fa-history',         ruta: '/medico/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user-md',         ruta: '/medico/perfil'    }
  ];

  constructor(private agendaService: AgendaService, private authService: AuthService) {}

  ngOnInit(): void {
    const u = this.authService.getUsuarioActual();
    this.nombreMedico = u ? `${u.nombres} ${u.apellidos}` : '';
    this.agendaService.getDashboardMedico().subscribe({
      next: data => { this.dashboardData = data; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }
}
