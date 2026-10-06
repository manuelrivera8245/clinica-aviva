import { Component, OnInit } from '@angular/core';
import { AgendaService, CitaResponse } from '../../services/agenda.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-gestionar-cita', templateUrl: './gestionar-cita.component.html', styleUrls: ['./gestionar-cita.component.css'] })
export class GestionarCitaComponent implements OnInit {
  citas: CitaResponse[] = [];
  cargando = true;
  filtroEstado = 'Programada';

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',       ruta: '/medico/dashboard' },
    { label: 'Agenda de Hoy', icon: 'fa-calendar-day',    ruta: '/medico/agenda'    },
    { label: 'Historial',     icon: 'fa-history',         ruta: '/medico/historial' },
    { label: 'Mi Perfil',     icon: 'fa-user-md',         ruta: '/medico/perfil'    }
  ];

  constructor(
    private agendaService: AgendaService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.cargando = true;
    this.agendaService.getHistorialMedico().subscribe({
      next: c  => { this.citas = c; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  marcarAtendido(cita: CitaResponse): void {
    this.agendaService.actualizarEstado({ idCita: cita.idCita, estado: 'Atendida' }).subscribe({
      next: () => { this.notificationService.exito('Cita marcada como Atendida.'); this.cargar(); },
      error: () => { this.notificationService.error('No se pudo actualizar el estado.'); }
    });
  }

  marcarNoAsistio(cita: CitaResponse): void {
    this.agendaService.actualizarEstado({ idCita: cita.idCita, estado: 'No Asistio' }).subscribe({
      next: () => { this.notificationService.warning('Cita marcada como No Asistió. El turno quedó liberado.'); this.cargar(); },
      error: () => { this.notificationService.error('No se pudo actualizar el estado.'); }
    });
  }

  get citasFiltradas(): CitaResponse[] {
    if (this.filtroEstado === 'todos') return this.citas;
    return this.citas.filter(c => c.estado === this.filtroEstado);
  }
}

