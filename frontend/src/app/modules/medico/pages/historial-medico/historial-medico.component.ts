import { Component, OnInit } from '@angular/core';
import { AgendaService, CitaResponse } from '../../services/agenda.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-historial-medico', templateUrl: './historial-medico.component.html', styleUrls: ['./historial-medico.component.css'] })
export class HistorialMedicoComponent implements OnInit {
  citas: CitaResponse[] = [];
  cargando = true;
  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',       ruta: '/medico/dashboard' },
    { label: 'Agenda de Hoy', icon: 'fa-calendar-day',    ruta: '/medico/agenda'    },
    { label: 'Historial',     icon: 'fa-history',         ruta: '/medico/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user-md',         ruta: '/medico/perfil'    }
  ];
  constructor(private agendaService: AgendaService) {}
  ngOnInit(): void {
    this.agendaService.getHistorialMedico().subscribe({
      next: c => { this.citas = c; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }
  get atendidas():  number { return this.citas.filter(c => c.estado === 'Atendida').length; }
  get noAsistio():  number { return this.citas.filter(c => c.estado === 'No Asistio').length; }
  get canceladas(): number { return this.citas.filter(c => c.estado === 'Cancelada').length; }
  get tasaAusentismo(): string {
    const total = this.citas.filter(c => c.estado !== 'Programada').length;
    if (total === 0) return '0%';
    return ((this.noAsistio / total) * 100).toFixed(1) + '%';
  }
}
