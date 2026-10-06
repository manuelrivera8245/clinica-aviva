import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-gestion-pacientes', templateUrl: './gestion-pacientes.component.html', styleUrls: ['./gestion-pacientes.component.css'] })
export class GestionPacientesComponent implements OnInit {
  pacientes: any[] = [];
  cargando = true;
  textoBusqueda = '';
  mostrarModal = false;
  pacienteADesactivar: any = null;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  constructor(private adminService: AdminService, private notificationService: NotificationService) {}

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.cargando = true;
    this.adminService.getPacientes().subscribe({
      next: p => { this.pacientes = p; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }

  get pacientesFiltrados(): any[] {
    if (!this.textoBusqueda.trim()) return this.pacientes;
    const q = this.textoBusqueda.toLowerCase();
    return this.pacientes.filter(p =>
      p.nombres?.toLowerCase().includes(q) ||
      p.apellidos?.toLowerCase().includes(q) ||
      p.dni?.includes(q) ||
      p.correo?.toLowerCase().includes(q)
    );
  }

  confirmarDesactivar(p: any): void { this.pacienteADesactivar = p; this.mostrarModal = true; }
  onConfirmacion(ok: boolean): void {
    this.mostrarModal = false;
    if (!ok || !this.pacienteADesactivar) return;
    this.adminService.desactivarPaciente(this.pacienteADesactivar.idPaciente).subscribe({
      next: () => { this.notificationService.exito('Paciente desactivado.'); this.cargar(); },
      error: () => this.notificationService.error('No se pudo desactivar.')
    });
    this.pacienteADesactivar = null;
  }
}
