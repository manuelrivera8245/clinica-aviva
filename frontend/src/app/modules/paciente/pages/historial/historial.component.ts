import { Component, OnInit } from '@angular/core';
import { CitaPacienteService } from '../../services/cita-paciente.service';
import { CitaResponse } from '../../models/paciente.model';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-historial',
  templateUrl: './historial.component.html',
  styleUrls: ['./historial.component.css']
})
export class HistorialComponent implements OnInit {
  citas: CitaResponse[] = [];
  cargando = true;
  filtroEstado = 'todos';

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-th-large',       ruta: '/paciente/dashboard' },
    { label: 'Reservar Cita', icon: 'fa-calendar-plus',  ruta: '/paciente/reservar'  },
    { label: 'Mis Citas',     icon: 'fa-calendar-check', ruta: '/paciente/mis-citas' },
    { label: 'Historial',     icon: 'fa-history',        ruta: '/paciente/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user',           ruta: '/paciente/perfil'    }
  ];

  constructor(private citaService: CitaPacienteService) {}

  ngOnInit(): void {
    this.citaService.getHistorial().subscribe({
      next: citas => { this.citas = citas; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get citasFiltradas(): CitaResponse[] {
    if (this.filtroEstado === 'todos') return this.citas;
    return this.citas.filter(c => c.estado === this.filtroEstado);
  }

  getClaseBadge(estado: string): string {
    const map: Record<string, string> = {
      'Programada': 'estado-programada',
      'Atendida':   'estado-atendida',
      'No Asistio': 'estado-no-asistio',
      'Cancelada':  'estado-cancelada'
    };
    return map[estado] || '';
  }
}
