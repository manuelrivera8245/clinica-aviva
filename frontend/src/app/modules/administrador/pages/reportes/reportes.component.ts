import { Component, OnInit } from '@angular/core';
import { AdminService, AusentismoMedicoItem } from '../../services/admin.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-reportes', templateUrl: './reportes.component.html', styleUrls: ['./reportes.component.css'] })
export class ReportesComponent implements OnInit {
  citas: any[] = [];
  ausentismo: AusentismoMedicoItem[] = [];
  cargando = false;
  desde = ''; hasta = '';

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    const hoy = new Date();
    const inicio = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    this.desde = inicio.toISOString().split('T')[0];
    this.hasta = hoy.toISOString().split('T')[0];
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    // Carga citas del período seleccionado
    this.adminService.getReporteCitas(this.desde, this.hasta).subscribe({
      next: c => { this.citas = c; this.cargando = false; },
      error: () => { this.cargando = false; }
    });
    // Carga ausentismo por medico
    this.adminService.getReporteAusentismoPorMedico(this.desde, this.hasta)
      .subscribe(a => this.ausentismo = a);
  }

  get totalAtendidas():  number { return this.citas.filter(c => c.estado === 'Atendida').length; }
  get totalCanceladas(): number { return this.citas.filter(c => c.estado === 'Cancelada').length; }
  get totalNoAsistio():  number { return this.citas.filter(c => c.estado === 'No Asistio').length; }
  get tasaAusentismo(): string {
    const cerradas = this.citas.filter(c => c.estado !== 'Programada').length;
    if (cerradas === 0) return '0.0%';
    return ((this.totalNoAsistio / cerradas) * 100).toFixed(1) + '%';
  }
}

