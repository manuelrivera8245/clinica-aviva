import { Component, OnInit } from '@angular/core';
import { AdminService, DashboardAdminData } from '../../services/admin.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-dashboard-admin', templateUrl: './dashboard.component.html', styleUrls: ['./dashboard.component.css'] })
export class DashboardAdminComponent implements OnInit {
  data: DashboardAdminData | null = null;
  cargando = true;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard', icon: 'fa-chart-bar', ruta: '/admin/dashboard' },
    { label: 'Médicos', icon: 'fa-user-md', ruta: '/admin/medicos' },
    { label: 'Turnos', icon: 'fa-clock', ruta: '/admin/turnos' },
    { label: 'Pacientes', icon: 'fa-users', ruta: '/admin/pacientes' },
    { label: 'Reportes', icon: 'fa-file-alt', ruta: '/admin/reportes' },
    { label: 'Configuración', icon: 'fa-cog', ruta: '/admin/configuracion' }
  ];

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.adminService.getDashboard().subscribe({
      next: d => { this.data = d; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
  }
}
