import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { SidebarItem } from '../../../../shared/components/sidebar/sidebar.component';

@Component({ selector: 'app-gestion-turnos', templateUrl: './gestion-turnos.component.html', styleUrls: ['./gestion-turnos.component.css'] })
export class GestionTurnosComponent implements OnInit {
  turnos: any[] = [];
  medicos: any[] = [];
  cargando = true;
  form!: FormGroup;
  mostrarForm = false;
  mostrarModal = false;
  turnoAEliminar: any = null;

  readonly menuItems: SidebarItem[] = [
    { label: 'Dashboard',     icon: 'fa-chart-bar',   ruta: '/admin/dashboard' },
    { label: 'Médicos',       icon: 'fa-user-md',     ruta: '/admin/medicos'   },
    { label: 'Turnos',        icon: 'fa-clock',       ruta: '/admin/turnos'    },
    { label: 'Pacientes',     icon: 'fa-users',       ruta: '/admin/pacientes' },
    { label: 'Horarios',      icon: 'fa-calendar-alt', ruta: '/admin/horarios'  },
    { label: 'Reportes',      icon: 'fa-file-alt',    ruta: '/admin/reportes'  },
    { label: 'Configuración', icon: 'fa-cog',         ruta: '/admin/configuracion' }
  ];

  constructor(private adminService: AdminService, private notificationService: NotificationService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      idMedico:  [null, [Validators.required]],
      fecha:     ['', [Validators.required]],
      horaInicio:['', [Validators.required]],
      horaFin:   ['', [Validators.required]]
    });
    this.cargar();
    this.adminService.getMedicos().subscribe(m => this.medicos = m.filter((x: any) => x.activo));
  }

  cargar(): void {
    this.cargando = true;
    this.adminService.getTurnos().subscribe({ next: t => { this.turnos = t; this.cargando = false; }, error: () => { this.cargando = false; } });
  }

  guardar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.adminService.crearTurno(this.form.value).subscribe({
      next: () => { this.notificationService.exito('Turno creado exitosamente.'); this.mostrarForm = false; this.form.reset(); this.cargar(); },
      error: err => this.notificationService.error(err?.error?.message || 'Error al crear turno.')
    });
  }

  confirmarEliminar(t: any): void { this.turnoAEliminar = t; this.mostrarModal = true; }
  onConfirmacion(ok: boolean): void {
    this.mostrarModal = false;
    if (!ok || !this.turnoAEliminar) return;
    this.adminService.eliminarTurno(this.turnoAEliminar.idTurno).subscribe({
      next: () => { this.notificationService.exito('Turno eliminado.'); this.cargar(); },
      error: () => this.notificationService.error('No se puede eliminar un turno con cita asociada.')
    });
    this.turnoAEliminar = null;
  }

  get f() { return this.form.controls; }
}
